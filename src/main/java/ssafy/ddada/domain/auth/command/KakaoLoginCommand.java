package ssafy.ddada.domain.auth.command;

import ssafy.ddada.common.kakao.model.KakaoToken;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.util.JwtParser;
import ssafy.ddada.domain.auth.model.PublicKey;

import java.util.List;

public record KakaoLoginCommand(
        String idToken,
        String oauthAccessToken,
        List<PublicKey> publicKeys,
        String kid,
        String iss,
        String aud
) {
    public static KakaoLoginCommand byKakao(KakaoToken token, List<PublicKey> publicKeys, JwtParser jwtParser, KakaoLoginProperties properties) {
        return new KakaoLoginCommand(
                token.idToken(),
                token.accessToken(),
                publicKeys,
                jwtParser.getKid(token.idToken()),
                properties.iss(),
                properties.clientId()
        );
    }
}
