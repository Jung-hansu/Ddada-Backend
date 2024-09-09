package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.entity.Gender;

public record MemberDetailResponse(
        @Schema(description = "프로필 이미지 경로", example = "https://my-bucket.s3.amazonaws.com/sample-image.jpg\n")
        String profilePreSignedUrl,

        @Schema(description = "닉네임", example = "쿠잉비")
        String nickname,

        @Schema(description = "성별", example = "MALE")
        String gender
) {
    public static MemberDetailResponse of(String profileImageBase64, String nickname, Gender gender) {
        return new MemberDetailResponse(profileImageBase64, nickname, gender.getValue());
    }
}
