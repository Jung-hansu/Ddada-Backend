package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSignupResponse(
        @Schema(description = "메시지", example = "회원가입이 성공적으로 처리되었습니다.")
        String message
) {
    public static MemberSignupResponse of(String message) {
        return new MemberSignupResponse(message);
    }
}
