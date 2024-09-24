package ssafy.ddada.domain.member.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.member.player.response.PlayerDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerProfileDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerMatchResponse;
import ssafy.ddada.api.member.player.response.PlayerSignupResponse;
import ssafy.ddada.common.exception.player.EmailDuplicateException;
import ssafy.ddada.common.exception.player.MemberNotFoundException;
import ssafy.ddada.common.exception.player.PasswordNotMatchException;
import ssafy.ddada.common.exception.security.InvalidPasswordException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.exception.security.PasswordUsedException;
import ssafy.ddada.common.exception.token.TokenSaveFailedException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.JwtProcessor;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.PasswordUpdateCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;
import ssafy.ddada.domain.member.player.entity.PasswordHistory;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public PlayerSignupResponse signupMember(MemberSignupCommand signupCommand) {
        Player tempPlayer = playerRepository.findByEmail(signupCommand.email())
                .orElse(null);
        if (tempPlayer != null && !tempPlayer.getIsDeleted()) {
            throw new EmailDuplicateException();
        }

        if (tempPlayer == null) {
            tempPlayer = new Player(
                    signupCommand.email(),
                    signupCommand.gender(),
                    signupCommand.birth(),
                    signupCommand.nickname(),
                    passwordEncoder.encode(signupCommand.password()),
                    null,
                    signupCommand.number(),
                    signupCommand.description(),
                    0,
                    MemberRole.PLAYER
            );
            playerRepository.save(tempPlayer);
        }

        String imageUrl = tempPlayer.getImage();

        if (signupCommand.imageUrl() != null) {
            imageUrl = s3Util.uploadImageToS3(signupCommand.imageUrl(), tempPlayer.getId(), "profileImg/");
        }

        String encodedPassword = passwordEncoder.encode(signupCommand.password());

        Player signupPlayer = tempPlayer.signupMember(signupCommand, imageUrl, encodedPassword);
        try {
            String accessToken = jwtProcessor.generateAccessToken(signupPlayer);
            String refreshToken = jwtProcessor.generateRefreshToken(signupPlayer);
            jwtProcessor.saveRefreshToken(accessToken, refreshToken);
            return PlayerSignupResponse.of(accessToken, refreshToken);
        } catch (Exception e) {
            throw new TokenSaveFailedException();
        }
    }

    @Override
    public PlayerDetailResponse getMemberDetail() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();

        // 프로필 이미지 경로 가져오기
        String profileImagePath = currentLoggedInPlayer.getImage();
        String preSignedProfileImage = "";

        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            preSignedProfileImage = s3Util.getPresignedUrlFromS3(imagePath);
        }

        Integer rating = currentLoggedInPlayer.getRating();

        // PlayerDetailResponse 반환
        return PlayerDetailResponse.of(
                preSignedProfileImage,
                currentLoggedInPlayer.getNickname(),
                rating
        );
    }

    @Override
    public PlayerProfileDetailResponse getMemberProfileDetail() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();
        String profileImagePath = currentLoggedInPlayer.getImage();

        String preSignedProfileImage = "";
        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            preSignedProfileImage = s3Util.getPresignedUrlFromS3(imagePath);
        }

        return PlayerProfileDetailResponse.of(
                preSignedProfileImage,
                currentLoggedInPlayer.getNickname(),
                currentLoggedInPlayer.getGender(),
                String.valueOf(currentLoggedInPlayer.getRating()),
                currentLoggedInPlayer.getNumber(),
                currentLoggedInPlayer.getEmail(),
                currentLoggedInPlayer.getDescription()
        );
}

    @Override
    @Transactional
    public PlayerDetailResponse updateMemberProfile(UpdateProfileCommand command) {
        Long userId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        Player currentLoggedInPlayer = playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        String imageUrl;

        // 만약 MultipartFile이 비어 있으면 기존 이미지 사용, 그렇지 않으면 새 이미지 업로드
        if (command.profileImagePath() == null || command.profileImagePath().isEmpty()) {
            imageUrl = currentLoggedInPlayer.getImage(); // 기존 이미지 사용
        } else {
            // 새 이미지를 업로드하고 새로운 이미지 URL을 얻음
            imageUrl = s3Util.uploadImageToS3(command.profileImagePath(), currentLoggedInPlayer.getId(), "profileImg/");
        }

        // presigned URL 생성
        String presignedUrl = "";
        if (imageUrl != null) {
            String imagePath = imageUrl.substring(imageUrl.indexOf("profileImg/"));
            presignedUrl = s3Util.getPresignedUrlFromS3(imagePath);
        }
        String nickname = command.nickname();
        if (command.nickname() != null && !command.nickname().isEmpty()) {
            nickname = currentLoggedInPlayer.getNickname();
        }

        // 프로필 정보 업데이트
        currentLoggedInPlayer.updateProfile(nickname, imageUrl, command.description());
        playerRepository.save(currentLoggedInPlayer);

        // presignedUrl을 반환
        return PlayerDetailResponse.of(
                presignedUrl,
                currentLoggedInPlayer.getNickname(),
                currentLoggedInPlayer.getRating()
        );
    }

    @Override
    @Transactional
    public String deleteMember() {
        getCurrentLoggedInMember().delete();
        return "회원 탈퇴가 성공적으로 처리되었습니다.";
    }

    @Override
    public Boolean checkNickname(String nickname) {
        boolean isDuplicated = playerRepository.existsByNickname(nickname);
        log.debug(">>> 닉네임 중복 체크: {}, 중복 여부: {}", nickname, isDuplicated);
        return isDuplicated;
    }

    @Override
    @Transactional
    public String updateMemberPassword(PasswordUpdateCommand command) {

        validateNewPassword(command.newPassword());

        Player player;

        if (command.email() != null && !command.email().isEmpty()) {
            player = playerRepository.findByEmail(command.email())
                    .orElseThrow(MemberNotFoundException::new);
        }
        else {
            player = getCurrentLoggedInMember();
        }

        if (!passwordEncoder.matches(command.currentPassword(), player.getPassword())) {
            throw new PasswordNotMatchException();
        }

        for (PasswordHistory history : player.getPasswordHistories()) {
            if (passwordEncoder.matches(command.newPassword(), history.getPassword())) {
                throw new PasswordUsedException();
            }
        }

        String encodedPassword = passwordEncoder.encode(command.newPassword());

        player.updatePassword(encodedPassword);
        playerRepository.save(player);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }

    @Override
    public List<PlayerMatchResponse> getPlayerMatches() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember(); // 현재 로그인된 플레이어
        List<Match> matches = matchRepository.findMatchesByPlayerId(currentLoggedInPlayer.getId()); // 플레이어와 관련된 경기들

        return matches.stream()
                .map(match -> {
                    Integer avgRating = calculateAverageRating(match);
                    String myTeamAndNumber = getMyTeamAndNumber(match, currentLoggedInPlayer);
                    return PlayerMatchResponse.from(match, avgRating, myTeamAndNumber);
                })
                .toList();
    }

    private static String getMyTeamAndNumber(Match match, Player currentPlayer) {
        return getTeamAndPosition(match.getTeam1(), "A팀", currentPlayer)
                .or(() -> getTeamAndPosition(match.getTeam2(), "B팀", currentPlayer))
                .orElse("참가 정보 없음");
    }

    private static Optional<String> getTeamAndPosition(Team team, String teamName, Player currentPlayer) {
        if (team.getPlayer1() != null && team.getPlayer1().getId().equals(currentPlayer.getId())) {
            return Optional.of(teamName + " 1번");
        } else if (team.getPlayer2() != null && team.getPlayer2().getId().equals(currentPlayer.getId())) {
            return Optional.of(teamName + " 2번");
        }
        return Optional.empty();
    }

    private Integer calculateAverageRating(Match match) {
        int totalRating = 0;
        int playerCount = 0;

        // 팀 1 플레이어들의 레이팅 추가
        if (match.getTeam1().getPlayer1() != null) {
            totalRating += match.getTeam1().getPlayer1().getRating();
            playerCount++;
        }
        if (match.getTeam1().getPlayer2() != null) {
            totalRating += match.getTeam1().getPlayer2().getRating();
            playerCount++;
        }

        // 팀 2 플레이어들의 레이팅 추가
        if (match.getTeam2().getPlayer1() != null) {
            totalRating += match.getTeam2().getPlayer1().getRating();
            playerCount++;
        }
        if (match.getTeam2().getPlayer2() != null) {
            totalRating += match.getTeam2().getPlayer2().getRating();
            playerCount++;
        }

        // 플레이어가 없을 경우 0 반환, 그렇지 않으면 평균 레이팅 계산
        return playerCount > 0 ? totalRating / playerCount : 0;
    }
    @Override
    public List<PlayerMatchResponse> getPlayerCompleteMatches() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();
        List<Match> matches = matchRepository.findCompletedMatchesByPlayerId(currentLoggedInPlayer.getId());

        return matches.stream()
                .map(match -> {
                    Integer avgRating = calculateAverageRating(match);
                    String myTeamAndNumber = getMyTeamAndNumber(match, currentLoggedInPlayer);
                    return PlayerMatchResponse.from(match, avgRating, myTeamAndNumber);
                })
                .toList();
    }

    @Override
    public Long getPlayerId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(NotAuthenticatedException::new);
    }

    private Player getCurrentLoggedInMember() {
        Long userId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword.length() < 8 ||
                !newPassword.matches(".*[!@#\\$%^&*].*")) {
            throw new InvalidPasswordException();
        }
    }
}

