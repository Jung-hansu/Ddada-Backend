package ssafy.ddada.domain.auth.service;

import ssafy.ddada.api.auth.response.MemberTypeResponse;
import ssafy.ddada.api.auth.response.AuthResponse;
import ssafy.ddada.api.auth.request.TokenRefreshRequest;
import ssafy.ddada.domain.auth.command.*;

public interface AuthService {
    AuthResponse login(LoginCommand command);
    AuthResponse refresh(TokenRefreshRequest refreshToken);
    void logout();
    void sendSms(SmsCommand command);
    Boolean verifyCertificationCode(VerifyCommand command);
    MemberTypeResponse getMemberType();
    void sendEmail(GmailSendCommand gmailSendCommand);
}
