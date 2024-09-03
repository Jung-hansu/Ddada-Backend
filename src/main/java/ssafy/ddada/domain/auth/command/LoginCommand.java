package ssafy.ddada.domain.auth.command;

public record LoginCommand (
        String loginType,
        String email,
        String password
)
{}

