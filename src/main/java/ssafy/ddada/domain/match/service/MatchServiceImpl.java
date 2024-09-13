package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.util.ParameterUtil;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.command.MatchCreateCommand;
import ssafy.ddada.domain.match.command.MatchResultCommand;
import ssafy.ddada.domain.match.command.MatchStatusChangeCommand;
import ssafy.ddada.domain.match.command.TeamChangePlayerCommand;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.Set;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final CourtRepository courtRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;

    @Override
    public Page<MatchSimpleResponse> getMatchesByKeyword(String keyword, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Match> matchPage;

        if (ParameterUtil.isEmptyString(status)){
            matchPage = ParameterUtil.isEmptyString(keyword) ?
                    matchRepository.findAllMatches(pageable) :
                    matchRepository.findMatchesByKeyword(keyword, pageable);
        } else {
            MatchStatus matchStatus = MatchStatus.parse(status);

            if (matchStatus == null) {
                throw new InvalidMatchStatusException();
            }

            matchPage = ParameterUtil.isEmptyString(keyword) ?
                    matchRepository.findAllMatchesByStatus(matchStatus, pageable) :
                    matchRepository.findMatchesByKeywordAndStatus(keyword, matchStatus, pageable);
        }
        return matchPage.map(MatchSimpleResponse::from);
    }

    @Override
    public MatchDetailResponse getMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        return MatchDetailResponse.from(match);
    }

    @Override
    public SetDetailResponse getSetById(Long matchId, Integer setNumber) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        List<Set> set = match.getSets();

        if (setNumber <= 0 || setNumber > set.size()) {
            throw new InvalidSetNumberException();
        }
        return SetDetailResponse.from(set.get(setNumber - 1));
    }

    @Override
    @Transactional
    public void updateMatchStatus(MatchStatusChangeCommand command) {
        matchRepository.setMatchStatus(command.matchId(), command.status());
    }

    @Deprecated
    @Override
    public TeamDetailResponse getTeamByTeamNumber(Long matchId, Integer teamNumber) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(TeamNotFoundException::new);
        Team team;

        if (teamNumber == 1) {
            team = match.getTeam1();
        } else if (teamNumber == 2){
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }
        return TeamDetailResponse.from(team);
    }

    private void updateTeamPlayerCount(Team team){
        int playerCount = 0;

        if (team.getPlayer1() != null) {
            playerCount++;
        }

        if (team.getPlayer2() != null) {
            playerCount++;
        }
        team.setPlayerCount(playerCount);
    }

    private void updateTeamRating(Team team){
        Player player1 = team.getPlayer1();
        Player player2 = team.getPlayer2();
        int ratingAverage = 0;

        if (player1 != null) {
            ratingAverage += player1.getRating();
        }

        if (player2 != null) {
            ratingAverage += player2.getRating();
        }

        ratingAverage /= team.getPlayerCount();
        team.setRating(ratingAverage);
    }

    private void updateTeam(Team team){
        updateTeamPlayerCount(team);
        updateTeamRating(team);
    }

    @Override
    @Transactional
    public void updateTeamPlayer(Long matchId, TeamChangePlayerCommand command) {
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);
        Player player = playerRepository.findById(command.playerId())
                .orElseThrow(MemberNotFoundException::new);
        Team team;

        if (command.teamNumber() == 1){
            team = match.getTeam1();
        } else if (command.teamNumber() == 2){
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }

        if (command.playerNumber() == 1){
            team.setPlayer1(player);
        } else if (!match.getMatchType().isSingle() && command.playerNumber() == 2){
            team.setPlayer2(player);
        } else {
            throw new InvalidTeamPlayerNumberException();
        }

        updateTeam(team);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void createMatch(Long creatorId, MatchCreateCommand command) {
        Court court = courtRepository.findById(command.court_id())
                .orElseThrow(CourtNotFoundException::new);
        Player creator = playerRepository.findById(creatorId)
                .orElseThrow(MemberNotFoundException::new);
        Team team1 = teamRepository.save(Team.createNewTeam(creator));
        Team team2 = teamRepository.save(Team.createNewTeam());
        Match match = Match.createNewMatch(
                court,
                team1,
                team2,
                command.matchType(),
                command.matchDate(),
                command.matchTime()
        );

        matchRepository.save(match);
    }

    @Override
    public Page<MatchSimpleResponse> getMatchesByManagerId(Long managerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Match> matchPage = matchRepository.findMatchesByManagerId(managerId, pageable);

        return matchPage.map(MatchSimpleResponse::from);
    }

    @Override
    @Transactional
    public void allocateManager(Long matchId, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        matchRepository.setManager(matchId, manager);
    }

    @Override
    @Transactional
    public void saveMatch(Long matchId, MatchResultCommand command) {
//        TODO: 서비스 로직 구현하기
    }

}
