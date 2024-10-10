package ssafy.ddada.domain.member.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.api.member.player.response.*;
import ssafy.ddada.common.constant.global.S3_IMAGE;
import ssafy.ddada.common.exception.player.*;
import ssafy.ddada.common.exception.security.*;
import ssafy.ddada.common.exception.token.TokenSaveFailedException;
import ssafy.ddada.common.util.RankingUtil;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.common.util.JwtProcessor;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.RatingChange;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.member.player.command.*;
import ssafy.ddada.domain.member.player.entity.PasswordHistory;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;
    private final MatchRepository matchRepository;
    private final RankingUtil rankingUtil;

    @Override
    @Transactional
    public PlayerSignupResponse signupMember(MemberSignupCommand signupCommand) {
        Player existingPlayer = playerRepository.findByEmail(signupCommand.email())
                .orElse(null);

        if (isDuplicateEmail(existingPlayer)) {
            throw new EmailDuplicateException();
        }

        Player playerToSave = existingPlayer != null ? existingPlayer : createNewPlayer(signupCommand);
        MultipartFile imageFile = signupCommand.imageUrl();
        String imageUrl = handleProfileImage(imageFile, playerToSave.getId(), playerToSave.getImage());

        String encodedPassword = passwordEncoder.encode(signupCommand.password());
        playerToSave.signupMember(signupCommand, imageUrl, encodedPassword);
        playerRepository.save(playerToSave);

        rankingUtil.savePlayerToRanking(playerToSave);

        try {
            String accessToken = jwtProcessor.generateAccessToken(playerToSave);
            String refreshToken = jwtProcessor.generateRefreshToken(playerToSave);
            jwtProcessor.saveRefreshToken(accessToken, refreshToken);
            return PlayerSignupResponse.of(accessToken, refreshToken);
        } catch (Exception e) {
            throw new TokenSaveFailedException();
        }
    }

    @Override
    public PlayerDetailResponse getMemberDetail() {
        Player currentPlayer = getCurrentLoggedInMember();
        String preSignedProfileImage = generatePreSignedUrl(currentPlayer.getImage());

        return PlayerDetailResponse.of(
                preSignedProfileImage,
                currentPlayer.getNickname(),
                currentPlayer.getRating(),
                currentPlayer.getGameCount()
        );
    }

    @Override
    public PlayerProfileDetailResponse getMemberProfileDetail() {
        Player currentPlayer = getCurrentLoggedInMember();
        String preSignedProfileImage = generatePreSignedUrl(currentPlayer.getImage());

        Integer wins = playerRepository.countWinsByPlayerId(currentPlayer.getId());
        Integer losses = playerRepository.countLossesByPlayerId(currentPlayer.getId());

        return PlayerProfileDetailResponse.of(
                preSignedProfileImage,
                currentPlayer.getNickname(),
                currentPlayer.getGender(),
                currentPlayer.getRating(),
                currentPlayer.getGameCount(),
                currentPlayer.getNumber(),
                currentPlayer.getEmail(),
                currentPlayer.getDescription(),
                wins,
                losses
        );
    }

    @Override
    @Transactional
    public PlayerDetailResponse updateMemberProfile(UpdateProfileCommand command) {
        Player currentPlayer = getCurrentLoggedInMember();

        MultipartFile profileImageFile = command.profileImagePath();
        String imageUrl = S3_IMAGE.DEFAULT_URL;
        if (!command.deleteImage()) {
            imageUrl = handleProfileImage(profileImageFile, currentPlayer.getId(), currentPlayer.getImage());
        }// MultipartFile로 변경
        String preSignedUrl = generatePreSignedUrl(imageUrl);

        String updatedNickname = getUpdatedField(command.nickname(), currentPlayer.getNickname());
        String updatedDescription = getUpdatedField(command.description(), currentPlayer.getDescription());

        currentPlayer.updateProfile(updatedNickname, imageUrl, updatedDescription);
        playerRepository.save(currentPlayer);

        return PlayerDetailResponse.of(
                preSignedUrl,
                currentPlayer.getNickname(),
                currentPlayer.getRating(),
                currentPlayer.getGameCount()
        );
    }

    @Override
    @Transactional
    public String deleteMember() {
        Player currentPlayer = getCurrentLoggedInMember();
        currentPlayer.delete();
        playerRepository.save(currentPlayer);
        rankingUtil.removePlayerFromRanking(currentPlayer);
        return "회원 탈퇴가 성공적으로 처리되었습니다.";
    }

    @Override
    public boolean checkNickname(String nickname) {
        boolean isDuplicated = playerRepository.existsByNickname(nickname);
        log.debug(">>> 닉네임 중복 체크: {}, 중복 여부: {}", nickname, isDuplicated);
        return isDuplicated;
    }

    @Override
    @Transactional
    public String updateMemberPassword(PasswordUpdateCommand command) {
        validateNewPassword(command.newPassword());

        Player player = getPlayerForPasswordUpdate(command);

        ensureNewPasswordIsNotUsed(command.newPassword(), player);

        String encodedNewPassword = passwordEncoder.encode(command.newPassword());
        player.updatePassword(encodedNewPassword);
        playerRepository.save(player);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }

    @Override
    public List<PlayerMatchResponse> getPlayerMatches() {
        Player currentPlayer = getCurrentLoggedInMember();
        List<Match> matches = matchRepository.findMatchesByPlayerId(currentPlayer.getId());

        return matches.stream()
                .map(match -> createPlayerMatchResponse(match, currentPlayer))
                .toList();
    }

    @Override
    public List<PlayerMatchResponse> getPlayerCompleteMatches() {
        Player currentPlayer = getCurrentLoggedInMember();
        List<Match> matches = matchRepository.findCompletedMatchesByPlayerId(currentPlayer.getId());

        return matches.stream()
                .map(match -> createPlayerMatchResponse(match, currentPlayer))
                .toList();
    }

    @Override
    public PlayerIdResponse getPlayerId() {
        return PlayerIdResponse.of(SecurityUtil.getLoginMemberId().orElseThrow(NotAuthenticatedException::new));
    }

    @Override
    public PlayerTotalMatchResponse getPlayerTotalMatch() {
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return PlayerTotalMatchResponse.of(playerRepository.countMatchesByPlayerId(playerId));
    }

    @Override
    public List<PlayerRankingResponse> getPlayersRanking() {
        // 현재 로그인한 플레이어의 정보
        Player currentPlayer = getCurrentLoggedInMember();
        Integer currentPlayerRating = currentPlayer.getRating();
        String currentPlayerNickname = currentPlayer.getNickname();
        Integer currentPlayerRanking = rankingUtil.getPlayerRank(currentPlayerNickname).intValue();


        // 레디스에서 상위 플레이어들의 닉네임과 레이팅 정보를 가져옴
        List<List<Object>> topPlayers = rankingUtil.getAllPlayers(); // 2중 리스트로 [nickname, rating] 형태

        AtomicInteger rank = new AtomicInteger(1);

        // 플레이어 순위를 PlayerRankingResponse 객체로 변환
        List<PlayerRankingResponse> rankings = topPlayers.stream()
                .map(playerData -> {
                    String nickname = (String) playerData.get(0);  // nickname
                    Integer rating = ((Double) playerData.get(1)).intValue();
                    return PlayerRankingResponse.of(
                            rank.getAndIncrement(),
                            nickname,
                            rating
                    );
                })
                .collect(Collectors.toList());

        // 현재 플레이어 정보 추가
        rankings.add(PlayerRankingResponse.of(
                currentPlayerRanking,
                currentPlayerNickname,
                currentPlayerRating
        ));

        return rankings;
    }


    private boolean isDuplicateEmail(Player existingPlayer) {
        return existingPlayer != null && !existingPlayer.getIsDeleted();
    }

    private Player createNewPlayer(MemberSignupCommand command) {
        return Player.builder()
                .email(command.email())
                .gender(command.gender())
                .birth(command.birth())
                .nickname(command.nickname())
                .password(passwordEncoder.encode(command.password()))
                .number(command.number())
                .description(command.description())
                .rating(0)
                .build();
    }

    /**
     * 프로필 이미지를 처리합니다. MultipartFile이 제공되면 S3에 업로드하고, 그렇지 않으면 기존 이미지를 유지합니다.
     *
     * @param imageFile         업로드할 이미지 파일
     * @param playerId          플레이어 ID
     * @param existingImageUrl  기존 이미지 URL
     * @return 새로운 이미지 URL 또는 기존 이미지 URL
     */
    private String handleProfileImage(MultipartFile imageFile, Long playerId, String existingImageUrl) {
        // 이미지 파일이 없거나 비어있는 경우
        if (imageFile == null || imageFile.isEmpty()) {
            if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                return existingImageUrl; // 기존 이미지가 있으면 반환
            } else {
                return S3_IMAGE.DEFAULT_URL; // 기본 이미지 URL 반환
            }
        }
        // 이미지 파일이 있는 경우 S3에 업로드
        return s3Util.uploadImageToS3(imageFile, playerId, "profileImg/");
    }

    private String generatePreSignedUrl(String imageUrl) {
        if (imageUrl == null) {
            return "";
        }
        String imagePath = extractImagePath(imageUrl);
        return s3Util.getPresignedUrlFromS3(imagePath);
    }

    private String extractImagePath(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf("profileImg/"));
    }

    private String getUpdatedField(String newValue, String currentValue) {
        return (newValue == null || newValue.isEmpty()) ? currentValue : newValue;
    }

    private Player getCurrentLoggedInMember() {
        Long userId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Player getPlayerForPasswordUpdate(PasswordUpdateCommand command) {
        if (command.email() != null) {
            return playerRepository.findByEmail(command.email())
                    .orElseThrow(MemberNotFoundException::new);
        }
        Player player = getCurrentLoggedInMember();
        verifyCurrentPassword(command.currentPassword(), player);
        return getCurrentLoggedInMember();
    }

    private void verifyCurrentPassword(String currentPassword, Player player) {
        if (!passwordEncoder.matches(currentPassword, player.getPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    private void ensureNewPasswordIsNotUsed(String newPassword, Player player) {
        for (PasswordHistory history : player.getPasswordHistories()) {
            if (passwordEncoder.matches(newPassword, history.getPassword())) {
                throw new PasswordUsedException();
            }
        }
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword.length() < 8 || !newPassword.matches(".*[!@#\\$%^&*].*")) {
            throw new InvalidPasswordException();
        }
    }

    private PlayerMatchResponse createPlayerMatchResponse(Match match, Player currentPlayer) {
        Integer avgRating = calculateAverageRating(match);
        String myTeamAndNumber = determineTeamAndNumber(match, currentPlayer);

        RatingChange ratingChange = playerRepository.findFirstByPlayerIdAndMatchId(currentPlayer.getId(), match.getId());

        Integer myRatingChange = null;
        if (ratingChange != null) {
            myRatingChange = ratingChange.getRatingChange();
        }

        return PlayerMatchResponse.from(match, avgRating, myTeamAndNumber, myRatingChange);
    }

    private String determineTeamAndNumber(Match match, Player currentPlayer) {
        return findTeamAndNumber(match.getTeam1(), "A팀", currentPlayer)
                .or(() -> findTeamAndNumber(match.getTeam2(), "B팀", currentPlayer))
                .orElse("참가 정보 없음");
    }

    private Optional<String> findTeamAndNumber(Team team, String teamName, Player currentPlayer) {
        if (isPlayerInTeam(team.getPlayer1(), currentPlayer)) {
            return Optional.of(teamName + " 1번");
        } else if (isPlayerInTeam(team.getPlayer2(), currentPlayer)) {
            return Optional.of(teamName + " 2번");
        }
        return Optional.empty();
    }

    private boolean isPlayerInTeam(Player teamPlayer, Player currentPlayer) {
        return teamPlayer != null && teamPlayer.getId().equals(currentPlayer.getId());
    }

    private Integer calculateAverageRating(Match match) {
        int totalRating = 0;
        int playerCount = 0;

        for (Player player : getAllPlayersInMatch(match)) {
            if (player != null) {
                totalRating += player.getRating();
                playerCount++;
            }
        }

        return playerCount > 0 ? totalRating / playerCount : 0;
    }

    private List<Player> getAllPlayersInMatch(Match match) {
        List<Player> players = new ArrayList<>();
        if (match.getTeam1().getPlayer1() != null) {
            players.add(match.getTeam1().getPlayer1());
        }
        if (match.getTeam1().getPlayer2() != null) {
            players.add(match.getTeam1().getPlayer2());
        }
        if (match.getTeam2().getPlayer1() != null) {
            players.add(match.getTeam2().getPlayer1());
        }
        if (match.getTeam2().getPlayer2() != null) {
            players.add(match.getTeam2().getPlayer2());
        }
        return players;
    }
}
