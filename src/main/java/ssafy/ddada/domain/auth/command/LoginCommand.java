package ssafy.ddada.domain.auth.command;

public record LoginCommand (
        String authcode,
        String loginType,
        String email,
        String password
)
{}

