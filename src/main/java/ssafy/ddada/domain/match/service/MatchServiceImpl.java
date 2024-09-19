package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.exception.exception.court.CourtNotFoundException;
import ssafy.ddada.common.exception.exception.manager.ManagerNotFoundException;
import ssafy.ddada.common.exception.exception.match.*;
import ssafy.ddada.common.exception.exception.player.MemberNotFoundException;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;
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

    private boolean isReserved(Match match, Long memberId) {
        return Objects.equals(match.getTeam1().getPlayer1().getId(), memberId) ||
                Objects.equals(match.getTeam1().getPlayer2().getId(), memberId) ||
                Objects.equals(match.getTeam2().getPlayer1().getId(), memberId) ||
                Objects.equals(match.getTeam2().getPlayer2().getId(), memberId);
    }

    @Override
    public Page<MatchSimpleResponse> getFilteredMatches(Long memberId, MatchSearchCommand command) {
        Page<Match> matchPage = matchRepository.findMatchesByKeywordAndTypeAndStatus(
                command.keyword(),
                command.rankType(),
                command.matchTypes(),
                command.statuses(),
                command.pageable()
        );

        return matchPage.map(match -> MatchSimpleResponse.from(match, isReserved(match, memberId)));
    }

    @Override
    public MatchDetailResponse getMatchByIdWithInfos(Long matchId) {
        Match match = matchRepository.findByIdWithInfos(matchId)
                .orElseThrow(MatchNotFoundException::new);
        return MatchDetailResponse.from(match);
    }

    @Override
    public SetDetailResponse getSetsByIdWithInfos(Long matchId, Integer setNumber) {
        Set set = matchRepository.findSetsByIdWithInfos(matchId, setNumber)
                .orElseThrow(InvalidSetNumberException::new);

        return SetDetailResponse.from(set);
    }

    @Override
    @Transactional
    public void updateMatchStatus(MatchStatusChangeCommand command) {
        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(MatchNotFoundException::new);
        match.setStatus(command.status());
        matchRepository.save(match);
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

    private boolean isMatchFull(Match match){
        int totalPlayerCnt = match.getTeam1().getPlayerCount() + match.getTeam2().getPlayerCount();
        return match.getMatchType().isSingle() ? totalPlayerCnt == 2 : totalPlayerCnt == 4;
    }

    @Override
    @Transactional
    public void setTeamPlayer(Long matchId, Long playerId, Integer teamNumber) {
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);

        // 모집 안된 경기만 선수 등록 가능
        if (!match.getStatus().equals(MatchStatus.CREATED)) {
            throw new InvalidMatchStatusException();
        }

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

        // 경기 모집 완료 시 경기 상태 변경
        updateTeam(team);
        if (isMatchFull(match) && match.getManager() != null){
            match.setStatus(MatchStatus.RESERVED);
        }
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

        // 모집 완료된 경기에서 선수 취소 시 경기 상태 변경
        if (match.getStatus() == MatchStatus.RESERVED){
            match.setStatus(MatchStatus.CREATED);
        }
        updateTeam(team);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void createMatch(Long creatorId, MatchCreateCommand command) {
        Court court = courtRepository.findById(command.courtId())
                .orElseThrow(CourtNotFoundException::new);
        Player creator = playerRepository.findById(creatorId)
                .orElseThrow(MemberNotFoundException::new);
        Team team1 = teamRepository.save(Team.createNewTeam(creator));
        Team team2 = teamRepository.save(Team.createNewTeam());
        Match match = Match.createNewMatch(
                court,
                team1,
                team2,
                command.rankType(),
                command.matchType(),
                command.matchDate(),
                command.matchTime()
        );

        matchRepository.save(match);
    }

    @Override
    public Page<MatchSimpleResponse> getMatchesByManagerId(ManagerSearchMatchCommand command) {
        return matchRepository.findFilteredMatches(
                    command.managerId(),
                    command.keyword(),
                    command.todayOnly(),
                    command.statuses(),
                    command.pageable()
                )
                .map(match -> MatchSimpleResponse.from(match, true));
    }

    @Override
    @Transactional
    public void allocateManager(Long matchId, Long managerId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);

        match.setManager(manager);
        if (isMatchFull(match)){
            match.setStatus(MatchStatus.RESERVED);
        }
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void saveMatch(Long matchId, MatchResultCommand command) {
//        TODO: 서비스 로직 구현하기
    }

}
