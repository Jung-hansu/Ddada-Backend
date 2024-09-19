package ssafy.ddada.domain.member.manager.command;

public record ManagerSignupCommand(
        String nickname,
        String password,
        String email,
        String number,
        String description
) {
}
