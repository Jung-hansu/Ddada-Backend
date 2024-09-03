package ssafy.ddada.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import ssafy.ddada.domain.member.command.MemberLoginCommand;

public record MemberLoginRequest(
        @Schema(description = "이메일", example = "example@example.com", format = "email")
        @Email(message = "유저ID는 이메일 형식이어야 합니다.")
        String email,

        @Schema(description = "비밀번호")
        String password
) {
    public MemberLoginCommand toCommand() {
        return new MemberLoginCommand(email, password);
    }
}