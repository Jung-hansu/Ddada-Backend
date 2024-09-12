package ssafy.ddada.domain.member.player.service;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import ssafy.ddada.api.member.player.response.PlayerDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerSignupResponse;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.properties.S3Properties;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.JwtProcessor;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;
import ssafy.ddada.domain.member.player.command.UpdateProfileCommand;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;
import com.amazonaws.HttpMethod;

import java.time.Duration;
import java.util.Date;
import java.util.Calendar;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    @Override
    @Transactional
    public PlayerSignupResponse signupMember(MemberSignupCommand signupCommand) {
        Player tempPlayer = playerRepository.findByEmail(signupCommand.email())
                .orElse(null);

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
                    MemberRole.PLAYER
            );
            playerRepository.save(tempPlayer);
        }

        // 이미지 처리 로직 추가: MultipartFile이 비어 있으면 기본 이미지 사용
        String imageUrl = (signupCommand.imageUrl() == null || signupCommand.imageUrl().isEmpty())
                ? "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg" // 기본 이미지 경로
                : uploadImageToS3(signupCommand.imageUrl(), tempPlayer.getId()); // 이미지가 있으면 S3에 업로드

        String encodedPassword = passwordEncoder.encode(signupCommand.password());

        Player signupPlayer = tempPlayer.signupMember(signupCommand, imageUrl, encodedPassword);

        String accessToken = jwtProcessor.generateAccessToken(signupPlayer);
        String refreshToken = jwtProcessor.generateRefreshToken(signupPlayer);
        jwtProcessor.saveRefreshToken(accessToken, refreshToken);

        return PlayerSignupResponse.of(accessToken, refreshToken);
    }


    @Override
    public PlayerDetailResponse getMemberDetail() {
        Player currentLoggedInPlayer = getCurrentLoggedInMember();
        String profileImagePath = currentLoggedInPlayer.getProfileImg();

        String preSignedProfileImage = "";
        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            preSignedProfileImage = getPresignedUrlFromS3(imagePath);
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
        Long userId = SecurityUtil.getLoginMemberId();
        Player currentLoggedInPlayer = playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        String imageUrl;

        // 만약 MultipartFile이 비어 있으면 기존 이미지 사용, 그렇지 않으면 새 이미지 업로드
        if (command.profileImagePath() == null || command.profileImagePath().isEmpty()) {
            imageUrl = currentLoggedInPlayer.getProfileImg(); // 기존 이미지 사용
        } else {
            // 새 이미지를 업로드하고 새로운 이미지 URL을 얻음
            imageUrl = uploadImageToS3(command.profileImagePath(), currentLoggedInPlayer.getId());
        }

        // presigned URL 생성
        String presignedUrl = "";
        if (imageUrl != null) {
            String imagePath = imageUrl.substring(imageUrl.indexOf("profileImg/"));
            presignedUrl = getPresignedUrlFromS3(imagePath);
        }

        // 프로필 정보 업데이트
        currentLoggedInPlayer.updateProfile(command.nickname(), imageUrl);
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

    private Player getCurrentLoggedInMember() {
        Long userId = SecurityUtil.getLoginMemberId();
        return playerRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private String uploadImageToS3(MultipartFile image, Long memberId) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif", ".PNG", ".JPG", ".JPEG", ".GIF");

        if (!allowedExtensions.contains(extension)) {
            throw new NotAllowedExtensionException();
        }

        String fileName = "profileImg/" + memberId + extension;

        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.s3().bucket(), fileName, image.getInputStream(), null));
            return amazonS3Client.getUrl(s3Properties.s3().bucket(), fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }

    private String getPresignedUrlFromS3(String imagePath) {
        try {
            String objectKey = imagePath.replace("https://ddada-image.s3.ap-northeast-2.amazonaws.com/", "");

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Properties.s3().bucket())
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            URL presignedUrl = presignedRequest.url();

            log.info(objectKey + " 이미지에 대한 presigned URL 생성 성공");
            log.info("Presigned URL: " + presignedUrl.toString());

            return presignedUrl.toString();
        } catch (Exception e) {
            log.error("Presigned URL 생성 중 오류 발생: " + e.getMessage(), e);
            throw new RuntimeException("Presigned URL 생성 실패", e);
        }
    }
}
