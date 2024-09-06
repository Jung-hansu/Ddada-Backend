package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.auth.command.VerifyCommand;

public record VerifyRequest(
        @Schema(description = "사용자 전화번호", example = "01012345678")
        String phoneNum,

        @Schema(description = "인증 코드", example = "123456")
        String certificationCode
) {
        public VerifyCommand toCommand() {
                return new VerifyCommand(phoneNum, certificationCode);
        }
}
