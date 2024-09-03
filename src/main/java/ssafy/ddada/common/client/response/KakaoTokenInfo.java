package ssafy.ddada.common.client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenInfo(
        String tokenType,
        String accessToken,
        String idToken,
        Long expiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn,
        String scope
) {
}
