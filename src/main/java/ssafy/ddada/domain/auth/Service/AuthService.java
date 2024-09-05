package ssafy.ddada.domain.auth.Service;

import org.apache.http.auth.InvalidCredentialsException;
import ssafy.ddada.api.auth.request.SmsRequest;
import ssafy.ddada.config.auth.AuthResponse;
import ssafy.ddada.config.auth.IdToken;
import ssafy.ddada.config.auth.TokenRefreshRequest;
import ssafy.ddada.domain.auth.command.LoginCommand;
import ssafy.ddada.domain.auth.command.LogoutCommand;
import ssafy.ddada.domain.auth.command.VerifyCommand;

public interface AuthService {
    IdToken getIdToken(String code);
    AuthResponse login(LoginCommand command) throws InvalidCredentialsException;
    AuthResponse refresh(TokenRefreshRequest refreshToken);
    void logout(LogoutCommand command);
    Boolean checknickname(String nickname);
    String sendSMS(SmsRequest smsRequest);
    Boolean verifyCertificationCode(VerifyCommand command);
}
