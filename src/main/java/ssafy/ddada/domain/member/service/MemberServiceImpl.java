package ssafy.ddada.domain.member.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
import ssafy.ddada.domain.member.repository.MemberRepository;

import java.io.IOException;
import java.util.Base64;
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
        // tempMember가 있는지 확인
        Member tempMember = memberRepository.findByEmail(signupCommand.email())
                .orElse(null);

        // tempMember가 없으면 새 유저 생성
        if (tempMember == null) {
            tempMember = new Member(
                    signupCommand.email(),
                    signupCommand.gender(),
                    signupCommand.birth(),
                    signupCommand.nickname(),
                    passwordEncoder.encode(signupCommand.password()),
                    null,
                    signupCommand.number(),
                    signupCommand.description()
            );
            memberRepository.save(tempMember);
        }

        String imageUrl = uploadImageToS3(signupCommand.imageUrl(), tempMember.getId());

        String encodedPassword = passwordEncoder.encode(signupCommand.password());

        // 회원가입 완료 후 회원 정보 갱신
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
        log.info(">>>>" + profileImagePath);

        String base64Image = "";
        if (profileImagePath != null) {
            String imagePath = profileImagePath.substring(profileImagePath.indexOf("profileImg/"));
            base64Image = getImageFromS3AsBase64(imagePath);
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

        String imageUrl = uploadImageToS3(command.profileImagePath(), currentLoggedInMember.getId());

        currentLoggedInMember.updateProfile(command.nickname(), imageUrl);
        memberRepository.save(currentLoggedInMember);

        return MemberDetailResponse.of(
                currentLoggedInMember.getProfileImg(),
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

    private String getImageFromS3AsBase64(String imagePath) {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucketName, imagePath);
            log.info(imagePath + " 이미지 가져오기 성공");
            byte[] imageBytes = IOUtils.toByteArray(s3Object.getObjectContent());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                return null;
            } else {
                throw new ProfileNotFoundInS3Exception();
            }
        } catch (IOException e) {
            throw new ProfileNotFoundInS3Exception();
        }
    }
}
