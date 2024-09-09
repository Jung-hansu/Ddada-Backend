package ssafy.ddada.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.auth.request.SmsRequest;
import ssafy.ddada.common.client.KakaoOauthClient;
import ssafy.ddada.common.client.response.KakaoTokenInfo;
import ssafy.ddada.common.exception.*;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.common.util.SmsCertificationUtil;
import ssafy.ddada.config.auth.*;
import ssafy.ddada.domain.auth.command.*;
import ssafy.ddada.domain.auth.model.LoginTokenModel;
import ssafy.ddada.domain.member.entity.Member;
import ssafy.ddada.domain.member.entity.MemberInterface;
import ssafy.ddada.domain.member.repository.CourtAdminRepository;
import ssafy.ddada.domain.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.repository.MemberRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.BASIC;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.KAKAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoLoginProperties kakaoLoginProperties;
    private final JwtParser jwtParser;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final CourtAdminRepository courtAdminRepository;
    private final ManagerRepository managerRepository;
    private final SmsCertificationUtil smsCertificationUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Long CERTIFICATION_CODE_EXPIRE_TIME = 3L;

    @Override
    public AuthResponse login(LoginCommand command) throws InvalidCredentialsException {
        LoginTokenModel tokens;
        log.info(">>> loginType: {}", command.authCode());

        switch (command.loginType()) {
            case KAKAO:
                KakaoLoginCommand kakaoLoginCommand = getKakaoLoginCommand(command.authCode());
                UserInfo userInfo = jwtParser.getUserInfo(kakaoLoginCommand);
                if (notRegisteredMember(userInfo)) {
                    return AuthResponse.notRegistered(userInfo);
                }
                log.debug(">>> hasSignupMember: {}", userInfo.email());

                MemberInterface kakaoMember = findMemberByEmail(userInfo.email())
                        .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + userInfo.email()));

                tokens = generateTokens(kakaoMember);
                jwtProcessor.saveRefreshToken(tokens);
                break;

            case BASIC:
                String email = command.email();
                String password = command.password();

                MemberInterface basicMember = findMemberByEmail(email)
                        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

                if (!passwordEncoder.matches(password, ((Member) basicMember).getPassword())) {
                    throw new InvalidCredentialsException("Invalid email or password");
                }

                tokens = generateTokens(basicMember);
                jwtProcessor.saveRefreshToken(tokens);
                break;

            default:
                throw new NotSupportedLoginTypeException();
        }

        return AuthResponse.of(tokens.accessToken(), tokens.refreshToken());
    }

    @Override
    public void logout(LogoutCommand command) {
        jwtProcessor.expireToken(command.accessToken());
    }

    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
        DecodedJwtToken decodedJwtToken = jwtProcessor.decodeToken(request.refreshToken(), REFRESH_TOKEN);
        MemberInterface member = findMemberById(decodedJwtToken)
                .orElseThrow(InvalidTokenException::new);

        String newAccessToken = jwtProcessor.generateAccessToken(member);
        String newRefreshToken = jwtProcessor.generateRefreshToken(member);

        jwtProcessor.renewRefreshToken(request.refreshToken(), newRefreshToken, member);
        return AuthResponse.of(newAccessToken, newRefreshToken);
    }

    public void sendSms(SmsRequest smsRequest) {
        String certificationCode = Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000);

        try {
            smsCertificationUtil.sendSMS(smsRequest.getPhoneNum(), certificationCode);
        } catch (MessageSendingException e) {
            throw new MessageSendingException();
        }

        redisTemplate.opsForValue().set(smsRequest.getPhoneNum(), certificationCode, CERTIFICATION_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    public Boolean verifyCertificationCode(VerifyCommand command) {
        String storedCode = redisTemplate.opsForValue().get(command.phoneNum());
        if (storedCode == null) {
            throw new SmsVerificationException();
        }
        return storedCode.equals(command.certificationCode());
    }

    private Boolean notRegisteredMember(UserInfo userInfo) {
        log.debug(">>> noSignupMember: {}", userInfo.email());

        if (memberRepository.existsByEmail(userInfo.email()) ||
                courtAdminRepository.existsByEmail(userInfo.email()) ||
                managerRepository.existsByEmail(userInfo.email())) {
            return isTempMember(findMemberByEmail(userInfo.email())
                    .orElseThrow(AbnormalLoginProgressException::new));
        }

        memberRepository.save(Member.createTempMember(userInfo.email()));
        return true;
    }

    private Optional<MemberInterface> findMemberByEmail(String email) {
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

    private LoginTokenModel generateTokens(MemberInterface member) {
        String accessToken = jwtProcessor.generateAccessToken(member);
        String refreshToken = jwtProcessor.generateRefreshToken(member);
        return new LoginTokenModel(accessToken, refreshToken);
    }

    private Boolean isTempMember(Object member) {
        if (member instanceof Member) {
            return ((Member) member).getNickname() == null;
        }
        return true;
    }

    private Optional<MemberInterface> findMemberById(DecodedJwtToken decodedJwtToken) {
        String role = decodedJwtToken.role();
        Long id = decodedJwtToken.memberId();
        log.info(">>> role: {}, id: {}", role, id);

        switch (role) {
            case "일반 유저":
                return memberRepository.findById(id)
                        .map(member -> (MemberInterface) member);

            case "코트관리자":
                return courtAdminRepository.findById(id)
                        .map(courtAdmin -> (MemberInterface) courtAdmin);

            case "매니저":
                return managerRepository.findById(id)
                        .map(manager -> (MemberInterface) manager);

            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }



    private KakaoLoginCommand getKakaoLoginCommand(String code) {
        log.info(">>> code: {}", code);
        KakaoTokenInfo kakaoTokenInfo = getKakaoToken(code);
        log.info(">>> kakaoTokenInfo: {}", kakaoTokenInfo);
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
}
