package ssafy.ddada.config.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record IdToken(
        @Schema(description = "ID 토큰", example = "W5JymSjmPDP4gqF5DgvsNnNKgI1K9_FFLWA4apetuLmIeL1RSWvKDwAAAAQKPCKcAAABkLSwn_stjdRiIM79qQ")
        String idToken
) {
    public static IdToken of(String idToken) {
        return new IdToken(idToken);
    }
}
