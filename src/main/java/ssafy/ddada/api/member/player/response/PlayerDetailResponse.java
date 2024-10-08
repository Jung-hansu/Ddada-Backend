package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선수 정보 응답 DTO")
public record PlayerDetailResponse(
        @Schema(description = "프로필 이미지 경로", example = "https://my-bucket.s3.amazonaws.com/sample-image.jpg\n")
        String profilePreSignedUrl,

        @Schema(description = "닉네임", example = "쿠잉비")
        String nickname,

        @Schema(description = "레이팅", example = "25")
        Integer rating,

        @Schema(description = "선수 경기 수")
        Integer gameCount
) {
    public static PlayerDetailResponse of(String profileImageBase64, String nickname, Integer rating, Integer gameCount) {
        return new PlayerDetailResponse(profileImageBase64, nickname, rating, gameCount);
    }
}
