package ssafy.ddada.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.auth.response.AuthResponse;
import ssafy.ddada.api.auth.request.TokenRefreshRequest;
import ssafy.ddada.api.auth.response.MemberTypeResponse;
import ssafy.ddada.common.kakao.client.KakaoOauthClient;
import ssafy.ddada.common.kakao.model.KakaoToken;
import ssafy.ddada.common.exception.player.LoginTypeNotSupportedException;
import ssafy.ddada.common.exception.player.*;
import ssafy.ddada.common.exception.security.InvalidTokenException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.exception.token.TokenSaveFailedException;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.util.JwtParser;
import ssafy.ddada.common.util.JwtProcessor;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.common.util.SmsCertificationUtil;
import ssafy.ddada.domain.auth.command.*;
import ssafy.ddada.domain.auth.model.DecodedJwtToken;
import ssafy.ddada.domain.auth.model.LoginToken;
import ssafy.ddada.domain.auth.model.UserInfo;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.gymadmin.repository.GymAdminRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static ssafy.ddada.common.constant.redis.CONST_VALUE.CERTIFICATION_CODE_EXPIRE_TIME;
import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.BASIC;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoLoginProperties kakaoLoginProperties;
    private final JwtParser jwtParser;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final GymAdminRepository gymAdminRepository;
    private final ManagerRepository managerRepository;
    private final SmsCertificationUtil smsCertificationUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public AuthResponse login(LoginCommand command) {
        log.info("[AuthService] 로그인 >>>> 로그인 타입: {}, 이메일: {}", command.loginType(), command.email());
        LoginToken tokens;

        switch (command.loginType()) {
            case KAKAO:
                KakaoLoginCommand kakaoLoginCommand = getKakaoLoginCommand(command.authCode());
                UserInfo userInfo = jwtParser.getUserInfo(kakaoLoginCommand);

                if (isNotRegisteredPlayer(userInfo)) {
                    return AuthResponse.notRegistered(userInfo);
                }

                log.debug("[AuthService] 가입된 멤버 정보 확인 >>>> 이름: {}, 이메일: {}", userInfo.nickname(), userInfo.email());
                Player kakaoMember = playerRepository.findNotDeletedPlayerByEmail(userInfo.email())
                        .orElseThrow(KakaoMailPlayerNotFoundException::new);

                if (kakaoMember.getIsDeleted()) {
                    return AuthResponse.notRegistered(userInfo);
                }

                tokens = generateTokens(kakaoMember);
                jwtProcessor.saveRefreshToken(tokens);
                break;

            case BASIC:
                String email = command.email();
                String password = command.password();

                Member basicMember = findMemberByEmail(email)
                        .orElseThrow(EmailNotFoundException::new);

                if (!passwordEncoder.matches(password, basicMember.getPassword())) {
                    throw new PasswordNotMatchException();
                }

                tokens = generateTokens(basicMember);
                jwtProcessor.saveRefreshToken(tokens);
                break;

            default:
                throw new LoginTypeNotSupportedException();
        }

        return AuthResponse.of(tokens.accessToken(), tokens.refreshToken());
    }

    @Override
    @Transactional
    public void logout() {
        log.info("[AuthService] 로그아웃");
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            jwtProcessor.expireToken(token);
        } else {
            throw new IllegalArgumentException("Invalid or missing Authorization header");
        }
    }

    @Override
    @Transactional
    public AuthResponse refresh(TokenRefreshRequest request) {
        log.info("[AuthService] Access 토큰 발급 >>>> Refresh 토큰: {}", request.refreshToken());
        DecodedJwtToken decodedJwtToken = jwtProcessor.decodeToken(request.refreshToken(), REFRESH_TOKEN);
        Member member = findMemberById(decodedJwtToken)
                .orElseThrow(InvalidTokenException::new);
        try {
            String newAccessToken = jwtProcessor.generateAccessToken(member);
            String newRefreshToken = jwtProcessor.generateRefreshToken(member);
            jwtProcessor.renewRefreshToken(request.refreshToken(), newRefreshToken, member);
            return AuthResponse.of(newAccessToken, newRefreshToken);
        }
        catch (Exception e) {
            log.error("[AuthService] Access 토큰 발급 중 오류 발생", e);
            throw new TokenSaveFailedException();
        }

    }

    @Override
    @Transactional
    public void sendSms(SmsCommand command) {
        log.info("[AuthService] 이메일 발송 >>>> 전화번호: {}", command.phoneNum());
        String certificationCode = generateCertificationCode();

        redisTemplate.opsForValue().set(command.phoneNum(), certificationCode, CERTIFICATION_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        smsCertificationUtil.sendSMS(command.phoneNum(), certificationCode);
    }

    @Override
    public Boolean verifyCertificationCode(VerifyCommand command) {
        log.info("[AuthService] Certification 코드 검사 >>>> 코드: {}", command.certificationCode());
        String storedCode;
        try {
            storedCode = redisTemplate.opsForValue().get(command.userInfo());
        } catch (Exception e) {
            throw new VerificationException();
        }

        if (storedCode == null || !storedCode.equals(command.certificationCode())) {
            throw new VerificationException();
        }

        return true;
    }

    @Override
    public MemberTypeResponse getMemberType() {
        log.info("[AuthService] 멤버 타입 조회");
        MemberRole memberType = SecurityUtil.getLoginMemberRole()
                .orElseThrow(NotAuthenticatedException::new);
        return MemberTypeResponse.of(memberType);
    }


    @Override
    public void sendEmail(GmailSendCommand gmailSendCommand) {
        log.info("[AuthService] 이메일 전송 >>>> 이메일: {}", gmailSendCommand.email());
        String certificationCode = generateCertificationCode();
        final String title = "DDADA 이메일 인증";
        final String text = "인증번호는 " + certificationCode + " 입니다.";

        redisTemplate.opsForValue().set(gmailSendCommand.email(), certificationCode, CERTIFICATION_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        try {
            SimpleMailMessage emailForm = createEmailForm(gmailSendCommand.email(), title, text);
            javaMailSender.send(emailForm);
            log.info("[AuthService] 이메일 발송 성공 >>>> 이메일 폼: {}", emailForm);
        } catch (Exception e) {
            log.error("이메일 발송 오류");
        }
    }

    private String generateCertificationCode(){
        return Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000);
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        log.debug("[AuthService] 이메일 폼 생성 >>>> 대상: {}, 제목: {}, 내용: {}", toEmail, title, text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        log.debug("[AuthService] 이메일 폼 생성 성공 >>>> 이메일: {}", message);
        return message;
    }

    private boolean isNotRegisteredPlayer(UserInfo userInfo) {
        log.debug("[AuthService] 미가입 선수 확인 >>>> 선수 이메일: {}", userInfo.email());
        String email = userInfo.email();
        Member member = findMemberByEmail(email).orElse(null);

        if (member == null){
            playerRepository.save(Player.createTempPlayer());
            return true;
        }
        return isTempPlayer(member);
    }

    private Optional<Member> findMemberByEmail(String email) {
        return playerRepository.findNotDeletedPlayerByEmail(email).map(member -> (Member) member)
                .or(() -> gymAdminRepository.findByEmail(email).map(member -> (Member) member))
                .or(() -> managerRepository.findByEmail(email).map(member -> (Member) member));
    }

    private LoginToken generateTokens(Member member) {
        String accessToken = jwtProcessor.generateAccessToken(member);
        String refreshToken = jwtProcessor.generateRefreshToken(member);
        return new LoginToken(accessToken, refreshToken);
    }

    private Boolean isTempPlayer(Member member) {
        return member.getRole() == MemberRole.TEMP;
    }

    private Optional<Member> findMemberById(DecodedJwtToken decodedJwtToken) {
        String role = decodedJwtToken.role();
        Long id = decodedJwtToken.memberId();
        log.debug("[AuthService] 멤버 ID 조회 >>>> 역할: {}, ID: {}", role, id);

        return switch (role) {
            case "PLAYER" -> playerRepository.findById(id)
                    .map(member -> (Member) member);
            case "GYM_ADMIN" -> gymAdminRepository.findById(id)
                    .map(gymAdmin -> (Member) gymAdmin);
            case "MANAGER" -> managerRepository.findById(id)
                    .map(manager -> (Member) manager);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    private KakaoLoginCommand getKakaoLoginCommand(String code) {
        log.debug("[AuthService] 카카오 로그인 토큰 발급 >>>> 인증 코드: {}", code);
        KakaoToken kakaoToken = getKakaoToken(code);
        log.debug("[AuthService] 카카오 로그인 토큰 발급 성공 >>>> 카카오 토큰: {}", kakaoToken);
        return KakaoLoginCommand.byKakao(kakaoToken, kakaoOauthClient.getPublicKeys(), jwtParser, kakaoLoginProperties);
    }

    private KakaoToken getKakaoToken(String code) {
        return kakaoOauthClient.getToken(
                kakaoLoginProperties.contentType(),
                kakaoLoginProperties.clientId(),
                kakaoLoginProperties.loginRedirectUri(),
                code,
                kakaoLoginProperties.clientSecret()
        );
    }
}
