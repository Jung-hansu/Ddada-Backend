package ssafy.ddada.domain.auth.command;

public record VerifyCommand(
        String phoneNum,
        String certificationCode
) {}