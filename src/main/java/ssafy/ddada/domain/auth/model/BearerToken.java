package ssafy.ddada.domain.auth.model;

import static ssafy.ddada.common.constant.requestheader.AUTHORIZATION.BEARER;

public record BearerToken(
        String accessToken
) {
    public static BearerToken of(String accessToken) {
        return new BearerToken(BEARER + accessToken);
    }
}
