package ssafy.ddada.api.gym.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.command.SettleAccountCommand;

@Schema(description = "수익 인출 요청 DTO")
public record SettleAccountRequest(
        @Schema(description = "계좌 주소", example = "123-456789-01-001")
        String AcountAddress
) {
    public SettleAccountCommand toCommand() {
        return new SettleAccountCommand(
                AcountAddress
        );
    }
}
