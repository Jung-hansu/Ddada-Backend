package ssafy.ddada.domain.auth.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.ddada.common.client.KakaoApiClient;
import ssafy.ddada.common.client.KakaoOauthClient;
import ssafy.ddada.common.client.response.KakaoTokenExpireResponse;
import ssafy.ddada.common.client.response.KakaoTokenInfo;
import ssafy.ddada.common.constant.requestheader.CONTENT_TYPE;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.config.auth.*;
import ssafy.ddada.config.auth.DecodedJwtToken;
import ssafy.ddada.domain.auth.command.BearerToken;
import ssafy.ddada.domain.auth.command.KakaoLoginCommand;
import ssafy.ddada.domain.auth.model.LoginTokenModel;
import ssafy.ddada.domain.member.Member;
import ssafy.ddada.domain.member.MemberRepository;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;
import static ssafy.ddada.common.constant.security.LoginType.KAKAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoApiClient kakaoApiClient;
    private final KakaoLoginProperties kakaoLoginProperties;
    private final JwtParser jwtParser;
    private final JwtProcessor jwtProcessor;
    private final MemberRepository memberRepository;

    @Override
    public AuthResponse loginv2(String loginType, String code) {
        LoginTokenModel tokens;

        switch(loginType) {
            case KAKAO:
                KakaoLoginCommand kakaoLoginCommand = getKakaoLoginCommand(code);
                UserInfo userInfo = jwtParser.getUserInfo(kakaoLoginCommand);
                if(!expireKakaoToken(kakaoLoginCommand)) {
                    throw new KakaoTokenExpireException();
                }
                if (notRegisteredMember(userInfo)) {
                    return AuthResponse.notRegistered(userInfo);
                }
                log.debug(">>> hasSignupMember: {}", userInfo.email());
                tokens = generateTokens(userInfo);
                jwtProcessor.saveRefreshToken(tokens);
                break;
            default:
                throw new NotSupportedLoginTypeException();
        }
        return AuthResponse.of(tokens.accessToken(), tokens.refreshToken());
    }

    private Boolean expireKakaoToken(KakaoLoginCommand kakaoLoginCommand) {
        BearerToken bearerToken = BearerToken.of(kakaoLoginCommand.oauthAccessToken());
        KakaoTokenExpireResponse expired = kakaoApiClient.expireAccessToken(CONTENT_TYPE.X_WWW_FORM_URLENCODED, bearerToken.accessToken());
        log.debug(">>> expireKakaoToken.id() : {}", expired.id());
        return expired.success();
    }

    private Boolean notRegisteredMember(UserInfo userInfo) {
        log.debug(">>> noSignupMember: {}", userInfo.email());
        if (memberRepository.existsByEmail(userInfo.email())) {
            return isTempMember(getMember(userInfo));
        }
        memberRepository.save(Member.createTempMember(userInfo.email()));
        return true;
    }

    private Member getMember(UserInfo userInfo) {
        return memberRepository.findByEmail(userInfo.email())
                .orElseThrow(AbnormalLoginProgressException::new);
    }

    private LoginTokenModel generateTokens(UserInfo userInfo) {
        Member member = getMember(userInfo);
        String accessToken = jwtProcessor.generateAccessToken(member);
        String refreshToken = jwtProcessor.generateRefreshToken(member);
        return new LoginTokenModel(accessToken, refreshToken);
    }

    private Boolean isTempMember(Member member) {
        return member.getNickName() == null;
    }

    @Override
    @Deprecated(since="0.2", forRemoval = true)
    public IdToken getIdToken(String loginType, String code) {
        switch(loginType) {
            case "kakao":
                KakaoTokenInfo kakaoTokenInfo = getKakaoToken(code);
                return IdToken.of(kakaoTokenInfo.idToken());
            default:
                throw new NotSupportedLoginTypeException();
        }
    }

    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
        String refreshToken = jwtProcessor.findRefreshTokenById(request.accessToken());
        DecodedJwtToken decodedJwtToken = jwtProcessor.decodeToken(refreshToken, REFRESH_TOKEN);
        Member member = memberRepository.findById(decodedJwtToken.memberId())
                .orElseThrow(InvalidTokenException::new);

        String newAccessToken = jwtProcessor.generateAccessToken(member);
        String newRefreshToken = jwtProcessor.generateRefreshToken(member); // refresh token rotation

        jwtProcessor.renewRefreshToken(request.accessToken(), newAccessToken, newRefreshToken);
        return AuthResponse.of(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String accessToken) {
        jwtProcessor.expireToken(accessToken);
    }

    private KakaoTokenInfo getKakaoToken(String code) {
        return kakaoOauthClient.getToken(
                kakaoLoginProperties.contentType(),
                kakaoLoginProperties.clientId(),
                kakaoLoginProperties.loginRedirectUri(),
                code,
                kakaoLoginProperties.clientSecret());
    }

    private KakaoLoginCommand getKakaoLoginCommand(String code) {
        KakaoTokenInfo kakaoTokenInfo = getKakaoToken(code);
        return KakaoLoginCommand.byKakao(kakaoTokenInfo, kakaoOauthClient.getPublicKeys(), jwtParser, kakaoLoginProperties);
    }

    @Override
    @Deprecated(since="0.2", forRemoval = true)
    public AuthResponse login(String loginType, IdToken request) {
        String idToken = request.idToken();
        String kid = jwtParser.getKid(idToken);
        PublicKeys publicKeys;
        String iss;
        String aud;
        log.debug("kid: {}", kid);

        switch (loginType) {
            case "kakao":
                publicKeys = kakaoOauthClient.getPublicKeys();
                iss = kakaoLoginProperties.iss();
                aud = kakaoLoginProperties.clientId();
                log.debug("iss: {}, aud: {}", iss, aud);
                break;

            // other login types ...
            default:
                throw new NotSupportedLoginTypeException();
        }
        PublicKey key = publicKeys.getKeys().stream()
                .filter(k -> k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(IncorrectIssuerTokenException::new);
        log.debug("key: {}", key);

        UserInfo userInfo = jwtParser.getUserInfo(idToken, PublicKeyGenerator.execute(key), iss, aud);
        log.debug("userInfo.email(): {}", userInfo.email());

        if (Boolean.FALSE.equals(memberRepository.existsByEmail(userInfo.email()))) {
            return AuthResponse.notRegistered(userInfo);
        }

        LoginTokenModel tokens = generateTokens(userInfo);

        jwtProcessor.saveRefreshToken(tokens);
        return AuthResponse.of(tokens.accessToken(), tokens.refreshToken());
    }

    @Override
    public Boolean checkNickname(String nickname) {
        boolean isDuplicated = memberRepository.existsByNickName(nickname);
        log.debug(">>> 닉네임 중복 체크: {}, 중복 여부: {}", nickname, isDuplicated);
        return isDuplicated;
    }
}
