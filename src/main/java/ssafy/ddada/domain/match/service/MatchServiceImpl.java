package ssafy.ddada.domain.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.constant.global.COURT;
import ssafy.ddada.common.constant.global.S3_IMAGE;
import ssafy.ddada.common.exception.data.DataNotFoundException;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.exception.gym.GymAdminNotFoundException;
import ssafy.ddada.common.exception.manager.ManagerAlreadyBookedException;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;
import ssafy.ddada.common.exception.manager.UnauthorizedManagerException;
import ssafy.ddada.common.exception.match.*;
import ssafy.ddada.common.exception.player.MemberNotFoundException;
import ssafy.ddada.common.exception.player.PlayerAlreadyBookedException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.properties.WebClientProperties;
import ssafy.ddada.common.util.RankingUtil;
import ssafy.ddada.common.util.RatingUtil;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;
import ssafy.ddada.domain.match.command.*;
import ssafy.ddada.domain.match.entity.*;
import ssafy.ddada.domain.match.repository.*;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.gymadmin.entity.GymAdmin;
import ssafy.ddada.domain.member.gymadmin.repository.GymAdminRepository;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;
import ssafy.ddada.domain.member.manager.command.ManagerMatchStatusChangeCommand;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ssafy.ddada.common.util.ParameterUtil.nullToZero;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final SetRepository setRepository;
    private final ScoreRepository scoreRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ManagerRepository managerRepository;
    private final GymAdminRepository gymAdminRepository;
    private final CourtRepository courtRepository;
    private final RatingChangeRepository ratingChangeRepository;

    private final RatingUtil ratingUtil;
    private final S3Util s3Util;
    private final RankingUtil rankingUtil;

    private final WebClient webClient;
    private final WebClientProperties webClientProperties;

    @Override
    public Page<MatchSimpleResponse> getFilteredMatches(MatchSearchCommand command) {
        log.info("[MatchService] 경기 리스트 조회");
        Long memberId = SecurityUtil.getLoginMemberId().orElse(null);
        Page<Match> matchPage = matchRepository.findMatchesByKeywordAndTypeAndStatus(
                command.keyword(),
                command.rankType(),
                command.matchTypes(),
                command.statuses(),
                command.region(),
                command.pageable()
        );

        return matchPage.map(match -> MatchSimpleResponse.from(
                match,
                isReserved(match, memberId),
                s3Util.getPresignedUrlFromS3(match.getCourt().getGym().getImage()))
        );
    }

    private boolean isReserved(Match match, Long memberId) {
        Player A1 = match.getTeam1().getPlayer1(), A2 = match.getTeam1().getPlayer2();
        Player B1 = match.getTeam2().getPlayer1(), B2 = match.getTeam2().getPlayer2();

        return A1 != null && Objects.equals(A1.getId(), memberId) ||
                A2 != null && Objects.equals(A2.getId(), memberId) ||
                B1 != null && Objects.equals(B1.getId(), memberId) ||
                B2 != null && Objects.equals(B2.getId(), memberId);
    }

    @Override
    public MatchDetailResponse getMatchByIdWithInfos(Long matchId) {
        log.info("[MatchService] 경기 조회 >>>> 경기 ID: {}", matchId);
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
        log.info("[MatchService] 경기 상태 변경 >>>> 경기 ID: {}, 변경할 경기 상태: {}", matchId, command.status());
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);

        if (command.status() == MatchStatus.PLAYING && match.getStatus() != MatchStatus.RESERVED){
            throw new InvalidMatchStatusException();
        }
        match.setStatus(command.status());
        match = matchRepository.save(match);
        log.info("[MatchService] 경기 상태 변경 성공 >>>> 변경된 경기 상태: {}", match.getStatus());
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
    public void setTeamPlayer(Long matchId, Integer teamNumber) {
        log.info("[MatchService] 선수를 팀에 배치 >>>> 경기 ID: {}, 배치할 팀 번호: {}", matchId, teamNumber);
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);

        // 모집 안된 경기만 선수 등록 가능
        if (isMatchFull(match)) {
            throw new TeamFullException();
        }

        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(MemberNotFoundException::new);

        // 같은 시간에 이미 경기가 있는지 확인
        int conflictCount = matchRepository.countByPlayerAndDateTime(player.getId(), match.getMatchDate(), match.getMatchTime());
        if (conflictCount > 0) {
            throw new PlayerAlreadyBookedException();
        }

        Team team = getTeamByTeamNumber(match, teamNumber);
        allocatePlayerToTeam(match, team, player);

        // 경기 모집 완료 시 경기 상태 변경
        updateTeam(team);
        if (isMatchFull(match) && match.getManager() != null) {
            match.setStatus(MatchStatus.RESERVED);
        }

        teamRepository.save(team);
    }

    private void allocatePlayerToTeam(Match match, Team team, Player player){
        switch (team.getPlayerCount()) {
            case 0 -> team.setPlayer1(player);
            case 1 -> {
                if (team.getPlayer1() != null) {
                    validateGender(match, team.getPlayer1(), player);
                    team.setPlayer2(player);
                } else {
                    validateGender(match, team.getPlayer2(), player);
                    team.setPlayer1(player);
                }
            }
            case 2 -> throw new TeamFullException();
            default -> throw new InvalidTeamNumberException();
        }
    }

    private void validateGender(Match match, Player player1, Player player2) {
        switch (match.getMatchType()) {
            case FEMALE_DOUBLE -> {
                if (player1.getGender() != Gender.FEMALE || player2.getGender() != Gender.FEMALE) {
                    throw new InvalidMatchTypeException();
                }
            }
            case MALE_DOUBLE -> {
                if (player1.getGender() != Gender.MALE || player2.getGender() != Gender.MALE) {
                    throw new InvalidMatchTypeException();
                }
            }
            case MIXED_DOUBLE -> {
                if (player1.getGender() == player2.getGender()) {
                    throw new InvalidTeamNumberException();
                }
            }
        }
    }

    @Override
    @Transactional
    public void unsetTeamPlayer(Long matchId, Integer teamNumber) {
        log.info("[MatchService] 선수를 팀에서 제외 >>>> 경기 ID: {}, 팀 번호: {}", matchId, teamNumber);
        Match match = matchRepository.findByIdWithTeams(matchId)
                .orElseThrow(TeamNotFoundException::new);

        // 비어있지 않은 경기만 선수 제외 가능
        if (isMatchEmpty(match)) {
            throw new InvalidTeamNumberException();
        }

        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(MemberNotFoundException::new);

        Team team = getTeamByTeamNumber(match, teamNumber);

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
    public void createMatch(MatchCreateCommand command) {
        log.info("[MatchService] 경기 생성");
        Long creatorId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);

        int conflictCount = matchRepository.countByPlayerAndDateTime(creatorId, command.matchDate(), command.matchTime());
        if (conflictCount > 0) {
            throw new PlayerAlreadyBookedException();
        }

        Player creator = playerRepository.findById(creatorId)
                .orElseThrow(MemberNotFoundException::new);
        Court court = courtRepository.findById(command.courtId())
                .orElseThrow(CourtNotFoundException::new);
        Team team1 = teamRepository.save(
                Team.builder()
                        .player1(creator)
                        .playerCount(1)
                        .rating(creator.getRating())
                        .build()
        );
        Team team2 = teamRepository.save(Team.builder().build());

        Match match = Match.builder()
                .court(court)
                .team1(team1)
                .team2(team2)
                .rankType(command.rankType())
                .matchType(command.matchType())
                .matchDate(command.matchDate())
                .matchTime(command.matchTime())
                .status(MatchStatus.CREATED)
                .build();

        matchRepository.save(match);
    }

    @Override
    public Page<MatchSimpleResponse> getMatchesByManagerId(ManagerSearchMatchCommand command) {
        log.info("[MatchService] 경기 조회");
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        Page<Match> matchPage = matchRepository.findFilteredMatches(
                managerId,
                command.keyword(),
                command.todayOnly(),
                command.statuses(),
                command.pageable()
        );

        return matchPage.map(match -> {
            String presignedUrl = s3Util.getPresignedUrlFromS3(match.getCourt().getGym().getImage());
            return MatchSimpleResponse.from(match, true, presignedUrl);
        });
    }

    @Override
    @Transactional
    public void allocateManager(Long matchId) {
        log.info("[MatchService] 매니저 할당");
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        if (match.getManager() != null) {
            throw new ManagerAlreadyExistException();
        }

        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        int conflictCount = matchRepository.countByManagerAndDateTime(managerId, match.getMatchDate(), match.getMatchTime());
        if (conflictCount > 0) {
            throw new ManagerAlreadyBookedException();
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(ManagerNotFoundException::new);
        match.setManager(manager);

        if (isMatchFull(match)) {
            match.setStatus(MatchStatus.RESERVED);
        }
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void deallocateManager(Long matchId) {
        log.info("[MatchService] 매니저 할당 해제 >>>> 경기 ID: {}", matchId);
        Match match = matchRepository.findById(matchId)
                .orElseThrow(MatchNotFoundException::new);
        if (match.getManager() == null) {
            throw new ManagerNotFoundException();
        }

        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        if (!Objects.equals(match.getManager().getId(), managerId)) {
            throw new UnauthorizedManagerException();
        }

        match.setManager(null);
        if (match.getStatus() == MatchStatus.RESERVED) {
            match.setStatus(MatchStatus.CREATED);
        }
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void saveMatch(Long matchId, MatchResultCommand matchCommand) {
        log.info("[MatchService] 경기 저장 >>>> 경기 ID: {}", matchId);
        Match match = getValidatedMatch(matchId);
        saveMatchResult(match, matchCommand);

        int winTeamNumber = match.getWinnerTeamNumber();
        int loseTeamNumber = 3 - winTeamNumber;

        int winTeamTotalScore = getTotalTeamScore(matchCommand, winTeamNumber);
        int loseTeamTotalScore = getTotalTeamScore(matchCommand, loseTeamNumber);

        // 팀 플레이어 점수 계산
        updatePlayersRatings(match, matchCommand, loseTeamTotalScore, winTeamTotalScore, true);
        updatePlayersRatings(match, matchCommand, loseTeamTotalScore, winTeamTotalScore, false);

        updateGymIncome(match);

        savedMatchAnalysis(matchId);
    }

    private Match getValidatedMatch(Long matchId){
        Match match = matchRepository.findByIdWithInfos(matchId)
                .orElseThrow(MatchNotFoundException::new);
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);

        if (match.getManager() == null || !Objects.equals(match.getManager().getId(), managerId)) {
            throw new UnauthorizedManagerException();
        }

        if (match.getStatus() != MatchStatus.PLAYING){
            throw new InvalidMatchStatusException();
        }

        return match;
    }

    private void saveMatchResult(Match match, MatchResultCommand matchCommand) {
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

        newMatch = matchRepository.save(newMatch);
        for (SetResultCommand setCommand : matchCommand.sets()){
            saveSetResult(newMatch, setCommand);
        }
    }

    private void saveSetResult(Match match, SetResultCommand setCommand) {
        Set newSet = Set.builder()
                .match(match)
                .setNumber(setCommand.setNumber())
                .setWinnerTeamNumber(setCommand.setWinnerTeamNumber())
                .team1Score(setCommand.team1Score())
                .team2Score(setCommand.team2Score())
                .build();

        newSet = setRepository.save(newSet);
        for (ScoreResultCommand scoreCommand : setCommand.scores()){
            saveScoreResult(newSet, scoreCommand);
        }
    }

    private void saveScoreResult(Set set, ScoreResultCommand scoreCommand) {
        Score newScore = Score.builder()
                .set(set)
                .scoreNumber(scoreCommand.scoreNumber())
                .earnedPlayer(scoreCommand.earnedPlayerNumber())
                .missedPlayer1(scoreCommand.missedPlayer1Number())
                .missedPlayer2(scoreCommand.missedPlayer2Number())
                .earnedType(scoreCommand.earnedType())
                .missedType(scoreCommand.missedType())
                .build();

        scoreRepository.save(newScore);
    }

    private Team getTeamByTeamNumber(Match match, Integer teamNumber){
        return switch (teamNumber){
            case 1 -> match.getTeam1();
            case 2 -> match.getTeam2();
            default -> throw new InvalidTeamNumberException();
        };
    }

    private Player getPlayerByPlayerNumber(Team team, Integer playerNumber){
        return switch (playerNumber){
            case 1 -> team.getPlayer1();
            case 2 -> team.getPlayer2();
            default -> throw new InvalidTeamPlayerNumberException();
        };
    }

    private int getTotalTeamScore(MatchResultCommand matchCommand, Integer teamNumber) {
        return matchCommand.sets().stream()
                .filter(set -> teamNumber.equals(set.setWinnerTeamNumber()))
                .mapToInt(set -> set.getTeamScoreByTeamNumber(teamNumber))
                .sum();
    }

    private void updatePlayersRatings(Match match, MatchResultCommand matchCommand, int loseTeamTotalScore, int winTeamTotalScore, boolean isWin) {
        int teamNumber = isWin ? match.getWinnerTeamNumber() : 3 - match.getWinnerTeamNumber();
        Team team = getTeamByTeamNumber(match, teamNumber);

        Team oppositeTeam = getTeamByTeamNumber(match, 3 - teamNumber);
        int oppositeTeamRating = RatingUtil.calculateTeamRating(oppositeTeam.getPlayers());

        for (Player player : team.getPlayers()) {
            player.incrementStreak(isWin);
            List<Integer> playerScoreList = calculatePlayerMatchStats(match, player, matchCommand);
            double earnedRate = winTeamTotalScore == 0 ? 0.5 : (double) playerScoreList.get(0) / winTeamTotalScore;
            double missedRate = loseTeamTotalScore == 0 ? 0.5 : (double) playerScoreList.get(1) / loseTeamTotalScore;

            // 플레이어의 레이팅 및 랭킹 업데이트
            int newRating = ratingUtil.updatePlayerRating(
                    player,
                    oppositeTeamRating,
                    winTeamTotalScore,
                    earnedRate,
                    missedRate,
                    true
            );
            player.setRating(newRating);
            player.setGameCount(player.getGameCount() + 1);
            playerRepository.save(player);
            rankingUtil.updatePlayerRating(player);

            // 레이팅 변화 기록
            RatingChange ratingChange = ratingChangeRepository
                    .findRatingChangeByMatchIdAndPlayerId(match.getId(), player.getId())
                    .orElse(
                            RatingChange.builder()
                                    .ratingChange(newRating - player.getRating())
                                    .player(player)
                                    .match(match)
                                    .build()
                    );
            ratingChange.setRatingChange(newRating - player.getRating());
            ratingChangeRepository.save(ratingChange);
        }
    }

    private List<Integer> calculatePlayerMatchStats(Match match, Player player, MatchResultCommand matchResultCommand) {
        int totalScore = 0;
        int totalMiss = 0;

        for (SetResultCommand setResult : matchResultCommand.sets()) {
            for (ScoreResultCommand scoreResult : setResult.scores()) {
                if (isMatchedPlayer(match, player, scoreResult.earnedPlayerNumber())) {
                    totalScore++;
                }
                if (isMatchedPlayer(match, player, scoreResult.missedPlayer1Number())) {
                    totalMiss++;
                }
                if (isMatchedPlayer(match, player, scoreResult.missedPlayer2Number())) {
                    totalMiss++;
                }
            }
        }

        return Arrays.asList(totalScore, totalMiss);
    }

    private boolean isMatchedPlayer(Match match, Player player, Integer teamPlayerNumber){
        if (teamPlayerNumber == null){
            return false;
        }

        Team team = getTeamByTeamNumber(match, teamPlayerNumber / 10);
        Player earnedPlayer = getPlayerByPlayerNumber(team, teamPlayerNumber % 10);
        return Objects.equals(player.getId(), earnedPlayer.getId());
    }

    private void updateGymIncome(Match match){
        GymAdmin gymAdmin = match.getCourt().getGym().getGymAdmin();
        Integer cumulativeIncome = nullToZero(gymAdmin.getCumulativeIncome());
        gymAdmin.setCumulativeIncome(cumulativeIncome + COURT.DEFAULT_PRICE);
        gymAdminRepository.save(gymAdmin);
    }

    @Override
    public boolean CheckPlayerBooked(CheckPlayerBookedCommand command) {
        log.info("[MatchService] 선수 예약 여부 확인");
        Long playerId = SecurityUtil.getLoginMemberId().orElseThrow(GymAdminNotFoundException::new);
        int conflictCount = matchRepository.countByPlayerAndDateTime(playerId, command.matchDate(), command.matchTime());
        if (conflictCount > 0) {
            throw new PlayerAlreadyBookedException();
        }
        return true;
    }

    private String getPlayerImage(Player player) {
        String defaultImageUrl = S3_IMAGE.DEFAULT_URL;

        if (player == null) {
            log.warn("플레이어 객체가 null입니다.");
            return null;
        }

        if (player.getImage() == null || player.getImage().isEmpty()) {
            log.warn("{}의 이미지 경로가 null 또는 비어있습니다. 기본 이미지 URL 반환: {}", player.getNickname(), defaultImageUrl);
            return s3Util.getPresignedUrlFromS3(defaultImageUrl);
        }
        return s3Util.getPresignedUrlFromS3(player.getImage());
    }

    private void savedMatchAnalysis(Long matchId) {
        String requestUrl = webClientProperties.url() + "add_match_analysis/" + matchId + "/";

        webClient.post()
                .uri(requestUrl)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(errorBody -> Mono.error(new DataNotFoundException()))
                );
    }
}