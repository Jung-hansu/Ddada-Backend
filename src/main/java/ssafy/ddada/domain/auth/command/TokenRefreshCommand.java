package ssafy.ddada.domain.auth.command;

public record TokenRefreshCommand(
        String refreshToken
) {
}
