package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import ssafy.ddada.domain.auth.command.LoginCommand;
import ssafy.ddada.domain.member.command.MemberLoginCommand;

public record  LoginRequest(
        @Schema(description = "인가코드", example = "oIZqkpBr0z8b-PmDRKlsyX05Hxs-XTNyUeSVXXTcVp-wUn4Q3yQhYwAAAAQKKwzUAAABkRB4UCIicpf3YNJZ6g")
        String authcode,

        @Schema(description = "loginType", example = "kakao")
        String loginType,

        @Schema(description = "이메일", example = "example@example.com", format = "email")
        @Email(message = "유저ID는 이메일 형식이어야 합니다.")
        String email,

        @Schema(description = "비밀번호")
        String password
) {
        public LoginCommand toCommand() {
                return new LoginCommand(loginType, email, password);
        }
}
