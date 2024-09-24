package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.exception.court.CourtNotFoundException;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;
import ssafy.ddada.common.exception.manager.UnauthorizedManagerException;
import ssafy.ddada.common.exception.match.*;
import ssafy.ddada.common.exception.player.MemberNotFoundException;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.match.entity.*;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.Comparator;
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
        Player A1 = match.getTeam1().getPlayer1(), A2 = match.getTeam1().getPlayer2();
        Player B1 = match.getTeam2().getPlayer1(), B2 = match.getTeam2().getPlayer2();

        if (A1 != null && Objects.equals(A1.getId(), memberId)){
            return true;
        }

        if (A2 != null && Objects.equals(A2.getId(), memberId)){
            return true;
        }

        if (B1 != null && Objects.equals(B1.getId(), memberId)){
            return true;
        }

        if (B2 != null && Objects.equals(B2.getId(), memberId)){
            return true;
        }

        return false;
    }

    @Override
    public Page<MatchSimpleResponse> getFilteredMatches(Long memberId, MatchSearchCommand command) {
        Page<Match> matchPage = matchRepository.findMatchesByKeywordAndTypeAndStatus(
                command.keyword(),
                command.rankType(),
                command.matchTypes(),
                command.statuses(),
                command.region(),
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
        if (team.getPlayerCount() == 0){
            team.setRating(0);
            return;
        }

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
        if (isMatchFull(match)) {
            throw new TeamFullException();
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

    private Score buildScoreFrom(Set set, MatchResultCommand.SetResultCommand.ScoreResultCommand scoreCommand) {
        return Score.builder()
                .set(set)
                .scoreNumber(scoreCommand.scoreNumber())
                .earnedPlayer(scoreCommand.earnedPlayer())
                .missedPlayer1(scoreCommand.missedPlayer1())
                .missedPlayer2(scoreCommand.missedPlayer2())
                .earnedType(scoreCommand.earnedType())
                .missedType(scoreCommand.missedType())
                .build();
    }

    private Set buildSetFrom(Match match, MatchResultCommand.SetResultCommand setCommand) {
        Set newSet = Set.builder()
                .match(match)
                .setNumber(setCommand.setNumber())
                .setWinnerTeamNumber(setCommand.setWinnerTeamNumber())
                .team1Score(setCommand.team1Score())
                .team2Score(setCommand.team2Score())
                .build();

        newSet.getScores().addAll(
                setCommand.scores()
                        .stream()
                        .map(scoreCommand -> buildScoreFrom(newSet, scoreCommand))
                        .sorted(Comparator.comparingInt(Score::getScoreNumber))
                        .toList()
        );
        return newSet;
    }

    private Match buildMatchFrom(Match match, MatchResultCommand matchCommand) {
        Match newMatch = Match.builder()
                .id(match.getId())
                .court(match.getCourt())
                .team1(match.getTeam1())
                .team2(match.getTeam2())
                .manager(match.getManager())
                .status(match.getStatus())
                .rankType(match.getRankType())
                .matchType(match.getMatchType())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .winnerTeamNumber(matchCommand.winnerTeamNumber())
                .team1SetScore(matchCommand.team1SetScore())
                .team2SetScore(matchCommand.team2SetScore())
                .build();

        newMatch.getSets().addAll(
                matchCommand.sets()
                        .stream()
                        .map(setCommand -> buildSetFrom(match, setCommand))
                        .sorted(Comparator.comparingInt(Set::getSetNumber))
                        .toList()
        );
        return newMatch;
    }

    @Override
    @Transactional
    public void saveMatch(Long matchId, Long managerId, MatchResultCommand matchCommand) {
        Match match = matchRepository.findByIdWithInfos(matchId)
                .orElseThrow(MatchNotFoundException::new);
        Manager manager = match.getManager();

        if (manager == null || !Objects.equals(manager.getId(), managerId)){
            throw new UnauthorizedManagerException();
        }

        Match newMatch = buildMatchFrom(match, matchCommand);
        matchRepository.save(newMatch);
    }

}
