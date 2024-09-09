package ssafy.ddada.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.auth.request.LoginRequest;
import ssafy.ddada.api.auth.request.LogoutRequest;
import ssafy.ddada.api.auth.request.SmsRequest;
import ssafy.ddada.api.auth.request.VerifyRequest;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.AuthResponse;
import ssafy.ddada.config.auth.TokenRefreshRequest;
import ssafy.ddada.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {
    private final AuthService authService;
    private final StringRedisTemplate redisTemplate;

    @Operation(summary = "로그인", description = "유저 정보를 이용하여 login type으로 로그인 합니다.")
    @PostMapping("/login")
    public CommonResponse<AuthResponse> login(
            @RequestBody LoginRequest request
    ) throws InvalidCredentialsException {
        AuthResponse loginResponse = authService.login(request.toCommand());
        return CommonResponse.ok(loginResponse);
    }

    @Operation(summary = "토큰 갱신", description = "refresh token으로 access token을 갱신합니다.")
    @PostMapping("/refresh")
    public CommonResponse<AuthResponse> refresh(
            @RequestBody TokenRefreshRequest request
    ) {
        AuthResponse rotateTokenResponse = authService.refresh(request);
        return CommonResponse.ok(rotateTokenResponse);
    }

    @Operation(summary = "로그아웃", description = "엑세스 토큰을 이용하여 login type으로 로그인 합니다.")
    @PostMapping("logout")
    public CommonResponse<Void> logout(
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request.toCommand());
        return CommonResponse.noContent();
    }

    @Operation(summary = "ping", description = "인증 서버가 정상적으로 동작하는지 확인합니다.")
    @GetMapping("/ping")
    public CommonResponse ping() {
        log.debug(">>> loginMemberId : {}", SecurityUtil.getLoginMemberId());
        return CommonResponse.ok("pong");
    }

    @Operation(summary = "SMS 인증 코드 전송", description = "사용자에게 인증 코드를 포함한 SMS를 전송합니다.")
    @PostMapping("/sms/send")
    public CommonResponse<String> sendSMS(
            @RequestBody SmsRequest smsRequest
    ) {
        authService.sendSms(smsRequest);  // SMS 전송 서비스 호출
        return CommonResponse.ok("문자를 전송했습니다.", null);
    }

    @Operation(summary = "SMS 인증 코드 검증", description = "사용자가 입력한 SMS 인증 코드를 검증합니다.")
    @PostMapping("/sms/verify")
    public CommonResponse<String> verifySMSCode(
            @RequestBody VerifyRequest verityRequest
    ) {
        Boolean result = authService.verifyCertificationCode(verityRequest.toCommand());

        // 메시지를 if 문 밖에서 선언하고 할당하도록 변경
        String message;
        if (result == true) {
            message = "인증에 성공했습니다.";
        } else {
            message = "인증에 실패했습니다.";
        }

        return CommonResponse.ok(message, null);
    }

    // Redis 연결 상태 확인을 위한 엔드포인트 추가
    @Operation(summary = "Redis 연결 확인", description = "Redis 서버와의 연결 상태를 확인합니다.")
    @GetMapping("/redis/check")
    public CommonResponse<String> checkRedisConnection() {
        try {
            String pingResponse = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(pingResponse)) {
                return CommonResponse.ok("Redis 연결 성공: " + pingResponse);
            } else {
                return CommonResponse.ok("Redis 연결 실패: " + pingResponse);
            }
        } catch (Exception e) {
            log.error("Redis 연결 오류", e);
            return CommonResponse.ok("Redis 연결 오류: " + e.getMessage());
        }
    }
}