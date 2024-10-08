package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.auth.command.VerifyCommand;

@Schema(description = "유효성 검사 DTO")
public record VerifyRequest(
        @Schema(description = "사용자 정보(전화번호 ro 이메일)", example = "01012345678")
        String userInfo,

        @Schema(description = "인증 코드", example = "123456")
        String certificationCode
) {
        public VerifyCommand toCommand() {
                return new VerifyCommand(userInfo, certificationCode);
        }
}
