package ssafy.ddada.domain.auth.model;

public record LoginToken(
        String accessToken,
        String refreshToken
) {
}
