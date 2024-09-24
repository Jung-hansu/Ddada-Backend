package ssafy.ddada.domain.auth.command;

import ssafy.ddada.common.client.response.KakaoTokenInfo;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.config.auth.JwtParser;
import ssafy.ddada.config.auth.PublicKeys;

public record KakaoLoginCommand(
        String idToken,
        String oauthAccessToken,
        PublicKeys publicKeys,
        String kid,
        String iss,
        String aud
) {
    public static KakaoLoginCommand byKakao(KakaoTokenInfo info, PublicKeys publicKeys, JwtParser jwtParser, KakaoLoginProperties properties) {
        return new KakaoLoginCommand(
                info.idToken(),
                info.accessToken(),
                publicKeys,
                jwtParser.getKid(info.idToken()),
                properties.iss(),
                properties.clientId()
        );
    }
}
