package ssafy.ddada.domain.member.command;

public record MemberLoginCommand (
    String email,
    String password
)
{}
