package ssafy.ddada.domain.auth.Service;

import ssafy.ddada.config.auth.AuthResponse;
import ssafy.ddada.config.auth.IdToken;
import ssafy.ddada.config.auth.TokenRefreshRequest;

public interface AuthService {
    IdToken getIdToken(String loginType, String code);
    AuthResponse login(String loginType, IdToken request);
    AuthResponse loginv2(String loginType, String code);
    AuthResponse refresh(TokenRefreshRequest refreshToken);
    void logout(String accessToken);
    Boolean checkNickname(String nickname);
}
