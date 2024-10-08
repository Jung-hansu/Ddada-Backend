package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선수 회원가입 응답 DTO")
public record PlayerSignupResponse(
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOisadfawevx12`3121345135")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOisadfawevx12`3121345135")
        String refreshToken
) {
    public static PlayerSignupResponse of(String accessToken , String refreshToken) {
        return new PlayerSignupResponse(accessToken, refreshToken);
    }
}
