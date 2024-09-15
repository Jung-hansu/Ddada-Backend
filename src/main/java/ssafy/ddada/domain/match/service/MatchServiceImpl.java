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
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.Set;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.List;
import java.util.Objects;

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

    private boolean isReserved(Match match, Long memberId, MemberRole memberRole) {
        return switch(memberRole){
            case PLAYER ->
                    Objects.equals(match.getTeam1().getPlayer1().getId(), memberId) ||
                    Objects.equals(match.getTeam1().getPlayer2().getId(), memberId) ||
                    Objects.equals(match.getTeam2().getPlayer1().getId(), memberId) ||
                    Objects.equals(match.getTeam2().getPlayer2().getId(), memberId);
            case MANAGER ->
                    Objects.equals(match.getManager().getId(), memberId);
            default -> false;
        };
    }

    @Override
    public Page<MatchSimpleResponse> getMatchesByKeyword(Long memberId, MemberRole memberRole,MatchSearchCommand command) {
        Page<Match> matchPage;

        if (command.status() == null){
            matchPage = ParameterUtil.isEmptyString(command.keyword()) ?
                    matchRepository.findAllMatches(command.pageable()) :
                    matchRepository.findMatchesByKeyword(command.keyword(), command.pageable());
        } else {
            matchPage = ParameterUtil.isEmptyString(command.keyword()) ?
                    matchRepository.findAllMatchesByStatus(command.status(), command.pageable()) :
                    matchRepository.findMatchesByKeywordAndStatus(command.keyword(), command.status(), command.pageable());
        }

        return matchPage.map(match -> {
            boolean isReserved = isReserved(match, memberId, memberRole);
            return MatchSimpleResponse.from(match, isReserved);
        });
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
    public void setTeamPlayer(Long matchId, Long playerId, Integer teamNumber) {
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(MemberNotFoundException::new);
        Team team;

        if (teamNumber == 1){
            team = match.getTeam1();
        } else if (teamNumber == 2){
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }

        if (team.getPlayer1() == null){
            team.setPlayer1(player);
        } else if (team.getPlayer2() == null){
            team.setPlayer2(player);
        } else {
            throw new TeamFullException();
        }

        updateTeam(team);
        teamRepository.save(team);
    }

    @Override
    public void unsetTeamPlayer(Long matchId, Long playerId, Integer teamNumber) {
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(MemberNotFoundException::new);
        Team team;

        if (teamNumber == 1){
            team = match.getTeam1();
        } else if (teamNumber == 2){
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }

        if (team.getPlayer1() == player){
            team.setPlayer1(null);
        } else if (team.getPlayer2() == player){
            team.setPlayer2(null);
        } else {
            throw new TeamPlayerNotFoundException();
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

        return matchPage.map(match -> MatchSimpleResponse.from(match, true));
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
