package ssafy.ddada.domain.member.player.command;

public record PasswordUpdateCommand (
        String currentPassword,
        String newPassword,
        String email
){
}
