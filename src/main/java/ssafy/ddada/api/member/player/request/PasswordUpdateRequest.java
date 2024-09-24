package ssafy.ddada.api.member.player.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.player.command.PasswordUpdateCommand;

public record PasswordUpdateRequest(
        @Schema(description = "현재 비밀번호")
        String currentPassword,

        @Schema(description = "변경할 비밀번호")
        String newPassword
) {
    public PasswordUpdateCommand toCommand() {
        return new PasswordUpdateCommand(currentPassword, newPassword);
    }
}
