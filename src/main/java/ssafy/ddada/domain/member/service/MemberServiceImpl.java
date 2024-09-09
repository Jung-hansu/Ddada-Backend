package ssafy.ddada.domain.member.service;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.api.member.response.MemberDetailResponse;
import ssafy.ddada.api.member.response.MemberSignupResponse;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.JwtProcessor;
import ssafy.ddada.domain.member.command.MemberSignupCommand;
import ssafy.ddada.domain.member.command.UpdateProfileCommand;
import ssafy.ddada.domain.member.entity.Member;
import ssafy.ddada.domain.member.entity.MemberRole;
import ssafy.ddada.domain.member.repository.MemberRepository;
import com.amazonaws.HttpMethod;

import java.util.Date;
import java.util.Calendar;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional
    public MemberSignupResponse signupMember(MemberSignupCommand signupCommand) {
        Member tempMember = memberRepository.findByEmail(signupCommand.email())
                .orElse(null);

        if (tempMember == null) {
            tempMember = new Member(
                    signupCommand.email(),
                    signupCommand.gender(),
                    signupCommand.birth(),
                    signupCommand.nickname(),
                    passwordEncoder.encode(signupCommand.password()),
                    null,
                    signupCommand.number(),
                    signupCommand.description(),
                    MemberRole.USER
            );
            memberRepository.save(tempMember);
        }

        // 이미지 처리 로직 추가: MultipartFile이 비어 있으면 기본 이미지 사용
        String imageUrl = (signupCommand.imageUrl() == null || signupCommand.imageUrl().isEmpty())
                ? "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg" // 기본 이미지 경로
                : uploadImageToS3(signupCommand.imageUrl(), tempMember.getId()); // 이미지가 있으면 S3에 업로드

        String encodedPassword = passwordEncoder.encode(signupCommand.password());

        Member signupMember = tempMember.signupMember(signupCommand, imageUrl, encodedPassword);

        String accessToken = jwtProcessor.generateAccessToken(signupMember);
        String refreshToken = jwtProcessor.generateRefreshToken(signupMember);
        jwtProcessor.saveRefreshToken(accessToken, refreshToken);

        return MemberSignupResponse.of(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public MemberDetailResponse getMemberDetail() {
        Member currentLoggedInMember = getCurrentLoggedInMember();
        String profileImagePath = currentLoggedInMember.getProfileImg();
        log.info(">>>> role" + SecurityUtil.getLoginMemberRole());
        log.info(">>>>" + profileImagePath);

        String base64Image = "";
        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            base64Image = getPresignedUrlFromS3(imagePath);
        }

        return MemberDetailResponse.of(
                base64Image,
                currentLoggedInMember.getNickname(),
                currentLoggedInMember.getGender()
        );
    }

    @Override
    @Transactional
    public MemberDetailResponse updateMemberProfile(UpdateProfileCommand command) {
        Long userId = SecurityUtil.getLoginMemberId();
        Member currentLoggedInMember = memberRepository.findById(userId)
                .orElseThrow(NotFoundMemberException::new);

        String imageUrl;

        // 만약 MultipartFile이 비어 있으면 기존 이미지 사용, 그렇지 않으면 새 이미지 업로드
        if (command.profileImagePath() == null || command.profileImagePath().isEmpty()) {
            imageUrl = currentLoggedInMember.getProfileImg(); // 기존 이미지 사용
        } else {
            // 새 이미지를 업로드하고 새로운 이미지 URL을 얻음
            imageUrl = uploadImageToS3(command.profileImagePath(), currentLoggedInMember.getId());
        }

        // presigned URL 생성
        String presignedUrl = "";
        if (imageUrl != null) {
            String imagePath = imageUrl.substring(imageUrl.indexOf("profileImg/"));
            presignedUrl = getPresignedUrlFromS3(imagePath);
        }

        // 프로필 정보 업데이트
        currentLoggedInMember.updateProfile(command.nickname(), imageUrl);
        memberRepository.save(currentLoggedInMember);

        // presignedUrl을 반환
        return MemberDetailResponse.of(
                presignedUrl, // 프리사인드 URL 전달
                currentLoggedInMember.getNickname(),
                currentLoggedInMember.getGender()
        );
    }

    @Override
    @Transactional
    public String deleteMember() {
        getCurrentLoggedInMember().deleteMember();
        return "회원 탈퇴가 성공적으로 처리되었습니다.";
    }

    @Override
    public Boolean checkNickname(String nickname) {
        boolean isDuplicated = memberRepository.existsBynickname(nickname);
        log.debug(">>> 닉네임 중복 체크: {}, 중복 여부: {}", nickname, isDuplicated);
        return isDuplicated;
    }

    private Member getCurrentLoggedInMember() {
        Long userId = SecurityUtil.getLoginMemberId();
        return memberRepository.findById(userId)
                .orElseThrow(NotFoundMemberException::new);
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
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), null));
            return amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }

    private String getPresignedUrlFromS3(String imagePath) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, imagePath)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(getExpirationTime());

            URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            log.info(imagePath + " 이미지에 대한 presigned URL 생성 성공");

            return presignedUrl.toString();
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                return null;
            } else {
                throw new ProfileNotFoundInS3Exception();
            }
        }
    }

    // presigned URL 만료 시간을 지정하는 메서드
    private Date getExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10); // 10분 후 만료되는 URL 설정
        return cal.getTime();
    }
}
