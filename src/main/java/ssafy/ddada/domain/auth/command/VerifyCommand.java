package ssafy.ddada.domain.auth.command;

public record VerifyCommand(
        String userInfo,
        String certificationCode
) {}