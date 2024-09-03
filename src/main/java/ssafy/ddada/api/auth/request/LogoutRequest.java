package ssafy.ddada.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LogoutRequest(
        @Schema(description = "엑세스 토큰", example = "oIZqkpBr0z8b-PmDRKlsyX05Hxs-XTNyUeSVXXTcVp-wUn4Q3yQhYwAAAAQKKwzUAAABkRB4UCIicpf3YNJZ6g")
        String accessToken
) {
}
