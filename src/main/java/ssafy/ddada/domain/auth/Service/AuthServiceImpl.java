package ssafy.ddada.domain.auth.Service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.auth.request.SmsRequest;
import ssafy.ddada.common.client.KakaoApiClient;
import ssafy.ddada.common.client.KakaoOauthClient;
import ssafy.ddada.common.client.response.KakaoTokenExpireResponse;
import ssafy.ddada.common.client.response.KakaoTokenInfo;
import ssafy.ddada.common.constant.requestheader.CONTENT_TYPE;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.util.SmsCertificationUtil;
import ssafy.ddada.config.auth.*;
import ssafy.ddada.config.auth.DecodedJwtToken;
import ssafy.ddada.domain.auth.command.BearerToken;
import ssafy.ddada.domain.auth.command.KakaoLoginCommand;
import ssafy.ddada.domain.auth.command.LoginCommand;
import ssafy.ddada.domain.auth.command.LogoutCommand;
import ssafy.ddada.domain.auth.model.LoginTokenModel;
import ssafy.ddada.domain.member.entity.Member;
import ssafy.ddada.domain.member.entity.MemberInterface;
import ssafy.ddada.domain.member.repository.CourtAdminRepository;
import ssafy.ddada.domain.member.repository.ManagerRepository;
import ssafy.ddada.domain.member.repository.MemberRepository;

import java.util.Optional;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;
import static ssafy.ddada.common.constant.security.LoginType.BASIC;
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
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final CourtAdminRepository courtAdminRepository;
    private final ManagerRepository managerRepository;
    private final SmsCertificationUtil smsCertificationUtil;

    @Override
    public AuthResponse login(LoginCommand command) throws InvalidCredentialsException {
        LoginTokenModel tokens;

        switch(command.loginType()) {
            case KAKAO:
                KakaoLoginCommand kakaoLoginCommand = getKakaoLoginCommand(command.authcode());
                UserInfo userInfo = jwtParser.getUserInfo(kakaoLoginCommand);
                if (!expireKakaoToken(kakaoLoginCommand)) {
                    throw new KakaoTokenExpireException();
                }
                if (notRegisteredMember(userInfo)) {
                    return AuthResponse.notRegistered(userInfo);
                }
                log.debug(">>> hasSignupMember: {}", userInfo.email());

                // KAKAO 로그인에서 조회한 사용자
                MemberInterface kakaoMember = findMemberByEmail(userInfo.email())
                        .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + userInfo.email()));

                tokens = generateTokens(userInfo.email(), kakaoMember); // 이메일과 사용자 정보 전달
                jwtProcessor.saveRefreshToken(tokens);
                break;

            case BASIC:
                String email = command.email();
                String password = command.password();

                // BASIC 로그인에서 조회한 사용자
                MemberInterface basicMember = findMemberByEmail(email)
                        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

                // 비밀번호 검증 (암호화된 비밀번호와 비교)
                if (!passwordEncoder.matches(password, ((Member) basicMember).getPassword())) {
                    throw new InvalidCredentialsException("Invalid email or password");
                }

                // 로그인 성공 시 토큰 생성
                tokens = generateTokens(email, basicMember); // 이메일과 사용자 정보 전달
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

    // 새로 추가된 메서드: SMS 인증을 위한 코드 발송
    public String sendSMS(SmsRequest smsRequest) {
        String certificationCode = Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드 생성
        smsCertificationUtil.sendSMS(smsRequest.getPhoneNum(), certificationCode); // SMS 인증 유틸리티를 사용하여 SMS 발송
        return "문자를 전송했습니다.";
    }

    private Boolean notRegisteredMember(UserInfo userInfo) {
        log.debug(">>> noSignupMember: {}", userInfo.email());

        // 세 종류의 저장소에서 체크
        if (memberRepository.existsByEmail(userInfo.email()) ||
                courtAdminRepository.existsByEmail(userInfo.email()) ||
                managerRepository.existsByEmail(userInfo.email())) {
            return isTempMember(findMemberByEmail(userInfo.email())
                    .orElseThrow(AbnormalLoginProgressException::new));
        }

        // 임시 개인 회원 저장
        memberRepository.save(Member.createTempMember(userInfo.email()));
        return true;
    }

    private Optional<MemberInterface> findMemberByEmail(String email) {
        // 각 엔티티에서 검색
        if (memberRepository.existsByEmail(email)) {
            return Optional.ofNullable(memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email)));
        } else if (courtAdminRepository.existsByEmail(email)) {
            return Optional.ofNullable(courtAdminRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email)));
        } else if (managerRepository.existsByEmail(email)) {
            return Optional.ofNullable(managerRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email)));
        }
        return Optional.empty();
    }

    private LoginTokenModel generateTokens(String email, MemberInterface member) {

        String accessToken = jwtProcessor.generateAccessToken(member);
        String refreshToken = jwtProcessor.generateRefreshToken(member);
        return new LoginTokenModel(accessToken, refreshToken);
    }

    private Boolean isTempMember(Object member) {
        if (member instanceof Member) {
            return ((Member) member).getNickname() == null;}
        return true;
    }

    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
        String refreshToken = jwtProcessor.findRefreshTokenById(request.RefreshToken());
        DecodedJwtToken decodedJwtToken = jwtProcessor.decodeToken(refreshToken, REFRESH_TOKEN);

        // 각 엔티티에서 사용자 찾기
        MemberInterface member = findMemberById(decodedJwtToken.memberId())
                .orElseThrow(InvalidTokenException::new);

        String newAccessToken = jwtProcessor.generateAccessToken(member);
        String newRefreshToken = jwtProcessor.generateRefreshToken(member);

        jwtProcessor.renewRefreshToken(request.RefreshToken(), newRefreshToken, member);
        return AuthResponse.of(newAccessToken, newRefreshToken);
    }

    private Optional<MemberInterface> findMemberById(Long id) {
        if (memberRepository.existsById(id)) {
            return Optional.of(memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + id)));
        } else if (courtAdminRepository.existsById(id)) {
            return Optional.of(courtAdminRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + id)));
        } else if (managerRepository.existsById(id)) {
            return Optional.of(managerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + id)));
        }
        return Optional.empty();
    }


    @Override
    public void logout(LogoutCommand command) {
        jwtProcessor.expireToken(command.accessToken());
    }

    @Override
    public Boolean checknickname(String nickname) {
        boolean isDuplicated = memberRepository.existsBynickname(nickname);

        log.debug(">>> 닉네임 중복 체크: {}, 중복 여부: {}", nickname, isDuplicated);
        return isDuplicated;
    }


    private KakaoLoginCommand getKakaoLoginCommand(String code) {
        KakaoTokenInfo kakaoTokenInfo = getKakaoToken(code);
        return KakaoLoginCommand.byKakao(kakaoTokenInfo, kakaoOauthClient.getPublicKeys(), jwtParser, kakaoLoginProperties);
    }

    private KakaoTokenInfo getKakaoToken(String code) {
        return kakaoOauthClient.getToken(
                kakaoLoginProperties.contentType(),
                kakaoLoginProperties.clientId(),
                kakaoLoginProperties.loginRedirectUri(),
                code,
                kakaoLoginProperties.clientSecret());
    }

    @Override
    @Deprecated(since="0.2", forRemoval = true)
    public IdToken getIdToken(String code) {
                KakaoTokenInfo kakaoTokenInfo = getKakaoToken(code);
                return IdToken.of(kakaoTokenInfo.idToken());
        }
}

