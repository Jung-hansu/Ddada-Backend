package ssafy.ddada.api.member.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ssafy.ddada.domain.member.manager.command.ManagerSignupCommand;

@Schema(description = "매니저 회원가입 요청 DTO")
public record ManagerSignupRequest (
        @Size(min = 2, max = 20)
        @Schema(description = "닉네임", example = "쿠잉비", minimum = "2", maximum = "20")
        String nickname,

        @NotNull
        @Schema(description = "비밀번호", example = "password123")
        String password,

        @Email
        @NotNull
        @Schema(description = "이메일", example = "manager@example.com")
        String email,

        @Schema(description = "연락처 번호", example = "010-1234-5678")
        String number,

        @NotNull
        @Schema(description = "설명", example = "관리자 설명을 입력하세요.")
        String description
) {
        public ManagerSignupCommand toCommand() {
                return new ManagerSignupCommand(nickname, password, email, number, description);
        }
}
