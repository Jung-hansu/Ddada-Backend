package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;
import ssafy.ddada.common.exception.manager.UnauthorizedManagerException;
import ssafy.ddada.common.exception.match.*;
import ssafy.ddada.common.exception.player.MemberNotFoundException;
import ssafy.ddada.common.util.RatingUtil;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.match.entity.*;
import ssafy.ddada.domain.match.repository.RatingChangeRepository;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;
import ssafy.ddada.domain.member.gymadmin.repository.GymAdminRepository;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;
import ssafy.ddada.domain.member.manager.command.ManagerMatchStatusChangeCommand;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.match.repository.TeamRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ssafy.ddada.common.util.ParameterUtil.nullToZero;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private static final int INCOME = 3000;

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final CourtRepository courtRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    private final GymAdminRepository gymAdminRepository;
    private final RatingUtil ratingUtil;
    private final RatingChangeRepository ratingChangeRepository;
    private final S3Util s3Util;

    private boolean isReserved(Match match, Long memberId) {
        Player A1 = match.getTeam1().getPlayer1(), A2 = match.getTeam1().getPlayer2();
        Player B1 = match.getTeam2().getPlayer1(), B2 = match.getTeam2().getPlayer2();

        if (A1 != null && Objects.equals(A1.getId(), memberId)) {
            return true;
        }

        if (A2 != null && Objects.equals(A2.getId(), memberId)) {
            return true;
        }

        if (B1 != null && Objects.equals(B1.getId(), memberId)) {
            return true;
        }

        if (B2 != null && Objects.equals(B2.getId(), memberId)) {
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

        return matchPage.map(match -> MatchSimpleResponse.from(match, isReserved(match, memberId), s3Util.getPresignedUrlFromS3(match.getCourt().getGym().getImage())));
    }

    @Override
    public MatchDetailResponse getMatchByIdWithInfos(Long matchId) {
        Match match = matchRepository.findByIdWithInfos(matchId)
                .orElseThrow(MatchNotFoundException::new);

        return MatchDetailResponse.from(
                match,
                s3Util.getPresignedUrlFromS3(match.getCourt().getGym().getImage()),
                getPlayerImage(match.getTeam1().getPlayer1()),
                getPlayerImage(match.getTeam1().getPlayer2()),
                getPlayerImage(match.getTeam2().getPlayer1()),
                getPlayerImage(match.getTeam2().getPlayer2())
        );
    }

    @Override
    @Transactional
    public void updateMatchStatus(Long matchId, ManagerMatchStatusChangeCommand command) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);

        if (command.status() == MatchStatus.PLAYING && match.getStatus() != MatchStatus.RESERVED){
            throw new InvalidMatchStatusException();
        }
        match.setStatus(command.status());
        matchRepository.save(match);
    }

    private void updateTeamPlayerCount(Team team) {
        int playerCount = 0;

        if (team.getPlayer1() != null) {
            playerCount++;
        }

        if (team.getPlayer2() != null) {
            playerCount++;
        }
        team.setPlayerCount(playerCount);
    }

    private void updateTeamRating(Team team) {
        if (team.getPlayerCount() == 0) {
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

    private void updateTeam(Team team) {
        updateTeamPlayerCount(team);
        updateTeamRating(team);
    }

    private boolean isMatchFull(Match match) {
        int totalPlayerCnt = match.getTeam1().getPlayerCount() + match.getTeam2().getPlayerCount();
        return match.getMatchType().isSingle() ? totalPlayerCnt == 2 : totalPlayerCnt == 4;
    }

    private boolean isMatchEmpty(Match match){
        int totalPlayerCnt = match.getTeam1().getPlayerCount() + match.getTeam2().getPlayerCount();
        return totalPlayerCnt == 0;
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

        // 같은 시간에 이미 경기가 있는지 확인
        int conflictCount = matchRepository.countByPlayerAndDateTime(player.getId(), match.getMatchDate(), match.getMatchTime());
        if (conflictCount > 0) {
            throw new PlayerAlreadyBookedException();
        }

        Team team;

        if (teamNumber == 1) {
            team = match.getTeam1();
        } else if (teamNumber == 2) {
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }

        if (team.getPlayer1() == null) {
            team.setPlayer1(player);
        } else if (team.getPlayer2() == null) {
            team.setPlayer2(player);
        } else {
            throw new TeamFullException();
        }

        // 경기 모집 완료 시 경기 상태 변경
        updateTeam(team);
        if (isMatchFull(match) && match.getManager() != null) {
            match.setStatus(MatchStatus.RESERVED);
        }
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void unsetTeamPlayer(Long matchId, Long playerId, Integer teamNumber) {
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(MemberNotFoundException::new);
        Team team;

        if (teamNumber == 1) {
            team = match.getTeam1();
        } else if (teamNumber == 2) {
            team = match.getTeam2();
        } else {
            throw new InvalidTeamNumberException();
        }

        if (team.getPlayer1() != null && Objects.equals(team.getPlayer1().getId(), player.getId())) {
            team.setPlayer1(null);
        } else if (team.getPlayer2() != null && Objects.equals(team.getPlayer2().getId(), player.getId())) {
            team.setPlayer2(null);
        } else {
            throw new TeamPlayerNotFoundException();
        }

        // 모집 완료된 경기에서 선수 취소 시 경기 상태 변경
        if (match.getStatus() == MatchStatus.RESERVED) {
            match.setStatus(MatchStatus.CREATED);
        }
        updateTeam(team);
        teamRepository.save(team);

        if (isMatchEmpty(match)){
            matchRepository.delete(match);
        }
    }

    @Override
    @Transactional
    public void createMatch(Long creatorId, MatchCreateCommand command) {
        int conflictCount = matchRepository.countByPlayerAndDateTime(creatorId, command.matchDate(), command.matchTime());
        if (conflictCount > 0) {
            throw new PlayerAlreadyBookedException();
        }
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
                .map(match -> MatchSimpleResponse.from(match, true, s3Util.getPresignedUrlFromS3(match.getCourt().getGym().getImage())));
    }

    @Override
    @Transactional
    public void allocateManager(Long matchId, Long managerId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);

        int conflictCount = matchRepository.countByManagerAndDateTime(manager.getId(), match.getMatchDate(), match.getMatchTime());
        if (conflictCount > 0) {
            throw new ManagerAlreadyBookedException();
        }

        match.setManager(manager);

        if (isMatchFull(match)) {
            match.setStatus(MatchStatus.RESERVED);
        }
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void deallocateManager(Long matchId, Long managerId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);

        if (match.getManager() == null) {
            throw new ManagerNotFoundException();
        }

        if (!Objects.equals(match.getManager().getId(), manager.getId())) {
            throw new UnauthorizedManagerException();
        }

        match.setManager(null);
        if (match.getStatus() == MatchStatus.RESERVED) {
            match.setStatus(MatchStatus.CREATED);
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
        if (manager == null || !Objects.equals(manager.getId(), managerId)) {
            throw new UnauthorizedManagerException();
        }

        if (match.getStatus() != MatchStatus.PLAYING){
            throw new InvalidMatchStatusException();
        }

        Match newMatch = buildMatchFrom(match, matchCommand);
        matchRepository.save(newMatch);

        // 팀 레이팅 업데이트
        Team winningTeam = (match.getWinnerTeamNumber() == 1) ? match.getTeam1() : match.getTeam2();
        Team losingTeam = (match.getWinnerTeamNumber() == 1) ? match.getTeam2() : match.getTeam1();

        int winningTeamRating = RatingUtil.calculateTeamRating(winningTeam.getPlayers());
        int losingTeamRating = RatingUtil.calculateTeamRating(losingTeam.getPlayers());

        // 팀의 총 점수 계산
        int winningTeamTotalScore = matchCommand.sets().stream()
                .filter(set -> set.setWinnerTeamNumber().equals(matchCommand.winnerTeamNumber()))
                .mapToInt(set -> set.setWinnerTeamNumber().equals(1) ? set.team1Score() : set.team2Score())
                .sum();

        int losingTeamTotalScore = matchCommand.sets().stream()
                .filter(set -> !set.setWinnerTeamNumber().equals(matchCommand.winnerTeamNumber()))
                .mapToInt(set -> set.setWinnerTeamNumber().equals(1) ? set.team1Score() : set.team2Score())
                .sum();

        // 플레이어 레이팅 업데이트
        for (Player player : winningTeam.getPlayers()) {
            Integer newRating = ratingUtil.updatePlayerRating(player, losingTeamRating, true, winningTeamTotalScore,winningTeamRating, losingTeamRating);

            // 레이팅 변화 기록
            RatingChange ratingChange = RatingChange.createRatingChange(newRating - player.getRating(), player, match);
            ratingChangeRepository.save(ratingChange);

            // 플레이어의 레이팅 업데이트
            player.setRating(newRating);
            playerRepository.save(player);
        }

        // 진 팀 플레이어 점수 계산
        for (Player player : losingTeam.getPlayers()) {
            player.incrementLoseStreak();
            List<Integer> playerScoreList=calculatePlayerMatchStats(matchCommand, player.getId());
            int earnedRate = playerScoreList.get(0)/winningTeamTotalScore;
            int missedRate = playerScoreList.get(1)/losingTeamTotalScore;
            Integer newRating = ratingUtil.updatePlayerRating(player, winningTeamRating, false, losingTeamTotalScore, earnedRate, missedRate);
            RatingChange ratingChange = ratingChangeRepository.findRatingChangeByMatchIdAndPlayerId(match.getId(), player.getId()).orElse(null);
            // 레이팅 변화 기록
            if (ratingChange == null) {
                ratingChange = RatingChange.createRatingChange(newRating - player.getRating(), player, match);
            }
            else {
                ratingChange.setRatingChange(newRating - player.getRating());
            }
            ratingChangeRepository.save(ratingChange);

            // 플레이어의 레이팅 업데이트
            player.setRating(newRating);
            playerRepository.save(player);
        }

        GymAdmin gymAdmin = match.getCourt().getGym().getGymAdmin();
        Integer cumulativeIncome = nullToZero(gymAdmin.getCumulativeIncome());
        gymAdmin.setCumulativeIncome(cumulativeIncome + INCOME);
        gymAdminRepository.save(gymAdmin);
    }

    public List<Integer> calculatePlayerMatchStats(MatchResultCommand matchResultCommand, Long playerId) {
        int totalScore = 0;   // 총 득점
        int totalMissed = 0;  // 총 실점

        // 각 세트에 대해 반복
        for (MatchResultCommand.SetResultCommand setResult : matchResultCommand.sets()) {
            // 각 세트의 점수 결과에 대해 반복
            for (MatchResultCommand.SetResultCommand.ScoreResultCommand scoreResult : setResult.scores()) {
                // 득점 확인
                if (scoreResult.earnedPlayer().longValue() == playerId) {
                    totalScore++;
                }

                // 실점 확인
                if (scoreResult.missedPlayer1() != null && scoreResult.missedPlayer1().longValue() == playerId) {
                    totalMissed++;
                }
                if (scoreResult.missedPlayer2() != null && scoreResult.missedPlayer2().longValue() == playerId) {
                    totalMissed++;
                }
            }
        }

        return Arrays.asList(totalScore, totalMissed);
    }

    private String getPlayerImage(Player player) {
        // 기본 이미지 경로
        String defaultImageUrl = "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg";

        // 플레이어가 null인 경우 기본 이미지 URL 반환
        if (player == null) {
            log.warn("플레이어 객체가 null입니다.");
            return null;
        }

        // 플레이어의 이미지 경로가 null이거나 비어있는 경우 기본 이미지 URL 반환
        if (player.getImage() == null || player.getImage().isEmpty()) {
            log.warn("{}의 이미지 경로가 null 또는 비어있습니다. 기본 이미지 URL 반환: {}", player.getNickname(), defaultImageUrl);
            return s3Util.getPresignedUrlFromS3(defaultImageUrl);
        }

        // S3에서 presigned URL 생성
        return s3Util.getPresignedUrlFromS3(player.getImage());
    }

}