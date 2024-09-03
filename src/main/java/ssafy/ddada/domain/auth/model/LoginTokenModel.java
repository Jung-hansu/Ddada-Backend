package ssafy.ddada.domain.auth.model;

public record LoginTokenModel(
        String accessToken,
        String refreshToken
) {
}
