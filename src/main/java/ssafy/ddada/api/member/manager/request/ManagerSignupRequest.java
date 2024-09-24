package ssafy.ddada.api.member.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.member.manager.command.ManagerSignupCommand;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerSignupRequest {

        @Schema(description = "닉네임", example = "쿠잉비", minimum = "2", maximum = "20")
        @Size(min = 2, max = 20)
        private String nickname;

        @Schema(description = "비밀번호", example = "password123")
        @NotNull
        private String password;

        @Schema(description = "이메일", example = "manager@example.com")
        @Email
        @NotNull
        private String email;

        @Schema(description = "연락처 번호", example = "010-1234-5678")
        private String number;

        @Schema(description = "설명", example = "관리자 설명을 입력하세요.")
        @NotNull
        private String description;

        public ManagerSignupCommand toCommand() {
                return new ManagerSignupCommand(nickname, password, email, number, description);
        }
}
