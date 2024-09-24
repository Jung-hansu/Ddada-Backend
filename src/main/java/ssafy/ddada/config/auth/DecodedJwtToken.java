package ssafy.ddada.config.auth;

public record DecodedJwtToken(
        Long memberId,
        String role,
        String type
) {
}
