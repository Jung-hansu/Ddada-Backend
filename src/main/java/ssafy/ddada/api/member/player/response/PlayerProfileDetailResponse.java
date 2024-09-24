package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.common.Gender;

public record PlayerProfileDetailResponse(
        @Schema(description = "프로필 이미지 경로", example = "https://my-bucket.s3.amazonaws.com/sample-image.jpg\n")
        String profilePreSignedUrl,

        @Schema(description = "닉네임", example = "쿠잉비")
        String nickname,

        @Schema(description = "성별", example = "MALE")
        String gender,

        @Schema(description = "레이팅", example = "25")
        String rating,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "이메일", example = "example@gmail.com")
        String email,

        @Schema(description = "한 줄 소개", example = "안녕하세요")
        String description
) {
    // 모든 필드를 받는 메서드
    public static PlayerProfileDetailResponse of(String profileImageBase64, String nickname, Gender gender, String rating, String phoneNumber, String email, String description) {
        return new PlayerProfileDetailResponse(
                profileImageBase64,
                nickname,
                gender != null ? gender.getValue() : null,
                rating,
                phoneNumber,
                email,
                description
        );
    }
}
