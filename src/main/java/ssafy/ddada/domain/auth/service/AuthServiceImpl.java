package ssafy.ddada.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.auth.request.SmsRequest;
import ssafy.ddada.api.auth.response.MemberTypeResponse;
import ssafy.ddada.common.client.KakaoOauthClient;
import ssafy.ddada.common.client.response.KakaoTokenInfo;
import ssafy.ddada.common.exception.player.LoginTypeNotSupportedException;
import ssafy.ddada.common.exception.player.*;
import ssafy.ddada.common.exception.security.InvalidTokenException;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.exception.token.TokenSaveFailedException;
import ssafy.ddada.common.properties.KakaoLoginProperties;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.common.util.SmsCertificationUtil;
import ssafy.ddada.config.auth.*;
import ssafy.ddada.domain.auth.command.*;
import ssafy.ddada.domain.auth.model.LoginTokenModel;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.courtadmin.repository.CourtAdminRepository;
import ssafy.ddada.domain.member.manager.repository.ManagerRepository;
import ssafy.ddada.domain.member.player.entity.Player;
import ssafy.ddada.domain.member.player.repository.PlayerRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.BASIC;
import static ssafy.ddada.common.constant.security.LOGIN_TYPE.KAKAO;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoLoginProperties kakaoLoginProperties;
    private final JwtParser jwtParser;
    private final JwtProcessor jwtProcessor;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final CourtAdminRepository courtAdminRepository;
    private final ManagerRepository managerRepository;
    private final SmsCertificationUtil smsCertificationUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;
    private static final Long CERTIFICATION_CODE_EXPIRE_TIME = 5L;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public AuthResponse login(LoginCommand command) {
        LoginTokenModel tokens;
        log.info(">>> loginType: {}", command.authCode());

        switch (command.loginType()) {
            case KAKAO:
                KakaoLoginCommand kakaoLoginCommand = getKakaoLoginCommand(command.authCode());
                UserInfo userInfo = jwtParser.getUserInfo(kakaoLoginCommand);

                if (notRegisteredPlayer(userInfo)) {
                    return AuthResponse.notRegistered(userInfo);
                }
                log.debug(">>> hasSignupMember: {}", userInfo.email());

                Player kakaoMember = playerRepository.findByEmail(userInfo.email())
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


                if (!passwordEncoder.matches(password, ((Member) basicMember).getPassword())) {
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
        // 헤더에서 Authorization 값 가져오기
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 토큰 값만 추출
            String token = authorizationHeader.substring(7);

            // JWT 토큰 만료 처리
            jwtProcessor.expireToken(token);
        } else {
            // Authorization 헤더가 없거나 올바르지 않을 경우 처리 로직
            throw new IllegalArgumentException("Invalid or missing Authorization header");
        }
    }

    @Transactional
    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
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
            throw new TokenSaveFailedException();
        }

    }
    @Transactional
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
        String storedCode = redisTemplate.opsForValue().get(command.userInfo());
        if (!storedCode.equals(command.certificationCode())) {
            throw new VerificationException();
        }
        return true;
    }

    public MemberTypeResponse getMemberType() {
        MemberRole memberType = SecurityUtil.getLoginMemberRole()
                .orElseThrow(NotAuthenticatedException::new);
        return MemberTypeResponse.of(memberType);
    }


    public void sendEmail(GmailSendCommand gmailSendCommand) {
        String title = "DDADA 이메일 인증";
        String certificationCode = Integer.toString((int) (Math.random() * (999999 - 100000 + 1)) + 100000);
        String text = "인증번호는 " + certificationCode + " 입니다.";
        redisTemplate.opsForValue().set(gmailSendCommand.email(), certificationCode, CERTIFICATION_CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        SimpleMailMessage emailForm = createEmailForm(gmailSendCommand.email(), title, text);
        try {
            javaMailSender.send(emailForm);
            log.info("이메일 발송 성공");
        } catch (Exception e) {
            log.error("이메일 발송 오류");
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    private Boolean notRegisteredPlayer(UserInfo userInfo) {
        log.debug(">>> noSignupMember: {}", userInfo.email());

        if (playerRepository.existsByEmail(userInfo.email()) ||
                courtAdminRepository.existsByEmail(userInfo.email()) ||
                managerRepository.existsByEmail(userInfo.email())) {
            return isTempPlayer(findMemberByEmail(userInfo.email())
                    .orElseThrow(AbnormalLoginProgressException::new));
        }

        playerRepository.save(Player.createTempPlayer(userInfo.email()));
        return true;
    }

    private Optional<Member> findMemberByEmail(String email) {
        return playerRepository.findByEmail(email).map(member -> (Member) member)
                .or(() -> courtAdminRepository.findByEmail(email).map(member -> (Member) member))
                .or(() -> managerRepository.findByEmail(email).map(member -> (Member) member));
    }

    private LoginTokenModel generateTokens(Member member) {
        String accessToken = jwtProcessor.generateAccessToken(member);
        String refreshToken = jwtProcessor.generateRefreshToken(member);
        return new LoginTokenModel(accessToken, refreshToken);
    }

    private Boolean isTempPlayer(Object member) {
        if (member instanceof Player) {
            return ((Player) member).getNickname() == null;
        }
        return true;
    }

    private Optional<Member> findMemberById(DecodedJwtToken decodedJwtToken) {
        String role = decodedJwtToken.role();
        Long id = decodedJwtToken.memberId();
        log.info(">>> role: {}, id: {}", role, id);

        return switch (role) {
            case "선수" -> playerRepository.findById(id)
                    .map(member -> (Member) member);
            case "코트관리자" -> courtAdminRepository.findById(id)
                    .map(courtAdmin -> (Member) courtAdmin);
            case "매니저" -> managerRepository.findById(id)
                    .map(manager -> (Member) manager);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
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
