package ssafy.ddada.api.member.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "매니저 회원가입 응답 DTO")
public record ManagerSignupResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjM2MzQwMjM2LCJleHAiOjE2MzYzNDAyMzZ9.1J7")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjMzNjMzNjM2LCJleHAiOjE2MzYzNDAyMzZ9.1J7")
        String refreshToken
) {
    public static ManagerSignupResponse of(String accessToken , String refreshToken) {
        return new ManagerSignupResponse(accessToken, refreshToken);
    }
}
