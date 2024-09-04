package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.auth.command.LogoutCommand;

public record LogoutRequest(
        @Schema(description = "엑세스 토큰", example = "oIZqkpBr0z8b-PmDRKlsyX05Hxs-XTNyUeSVXXTcVp-wUn4Q3yQhYwAAAAQKKwzUAAABkRB4UCIicpf3YNJZ6g")
        String accessToken,

        @Schema(description = "로그아웃 타입", example = "kakao")
        String logoutType
) {
        public LogoutCommand toCommand() {
                return new LogoutCommand(logoutType, accessToken);
        }
}
