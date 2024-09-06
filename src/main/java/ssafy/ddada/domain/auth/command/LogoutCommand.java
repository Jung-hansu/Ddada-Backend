package ssafy.ddada.domain.auth.command;

public record LogoutCommand (
    String logoutType,
    String accessToken
)
{}
