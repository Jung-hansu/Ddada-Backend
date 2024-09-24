package ssafy.ddada.domain.member.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.member.player.response.PlayerDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerMatchResponse;
import ssafy.ddada.api.member.player.response.PlayerSignupResponse;
import ssafy.ddada.common.exception.player.EmailDuplicateException;
import ssafy.ddada.common.exception.player.MemberNotFoundException;
import ssafy.ddada.common.exception.player.PasswordNotMatchException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.exception.token.TokenSaveFailedException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.JwtProcessor;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.repository.MatchRepository;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.PasswordUpdateCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.List;


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
        String profileImagePath = currentLoggedInPlayer.getImage();

        String preSignedProfileImage = "";
        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            preSignedProfileImage = s3Util.getPresignedUrlFromS3(imagePath);
        }

        return PlayerDetailResponse.of(
                preSignedProfileImage,
                currentLoggedInPlayer.getNickname(),
                currentLoggedInPlayer.getGender()
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

        // 프로필 정보 업데이트
        currentLoggedInPlayer.updateProfile(command.nickname(), imageUrl, command.phoneNumber(), command.description());
        playerRepository.save(currentLoggedInPlayer);

        // presignedUrl을 반환
        return PlayerDetailResponse.of(
                presignedUrl, // 프리사인드 URL 전달
                currentLoggedInPlayer.getNickname(),
                currentLoggedInPlayer.getGender()
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
    public String updateMemberPassword(PasswordUpdateCommand command) {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();

        if (!passwordEncoder.matches(command.currentPassword(), ((Member) currentLoggedInPlayer).getPassword())) {
            throw new PasswordNotMatchException();
        }

        String encodedPassword = passwordEncoder.encode(command.newPassword());
        currentLoggedInPlayer.updatePassword(encodedPassword);
        playerRepository.save(currentLoggedInPlayer);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }

    @Override
    public List<PlayerMatchResponse> getPlayerMatches() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();
        List<Match> matches = matchRepository.findMatchesByPlayerId(currentLoggedInPlayer.getId());

        return matches.stream()
                .map(PlayerMatchResponse::from)
                .toList();
    }

    @Override
    public Long getPlayerId() {
        Long userId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return userId;
    }

    private Player getCurrentLoggedInMember() {
        Long userId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        return playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);
    }
}

