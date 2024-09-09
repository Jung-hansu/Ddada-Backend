package ssafy.ddada.domain.member.player.command;

public record MemberLoginCommand (
    String email,
    String password
)
{}
