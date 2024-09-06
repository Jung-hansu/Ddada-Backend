package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSignupResponse(
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOisadfawevx12`3121345135")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOisadfawevx12`3121345135")
        String refreshToken
) {
    public static MemberSignupResponse of(String accessToken , String refreshToken) {
        return new MemberSignupResponse(accessToken, refreshToken);
    }
}
