package ssafy.ddada.api.auth;

import com.nimbusds.openid.connect.sdk.LogoutRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.auth.request.*;
import ssafy.ddada.api.auth.response.MemberTypeResponse;
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
    public CommonResponse<Void> logout() {
        authService.logout();
        return CommonResponse.noContent();
    }

    @Operation(summary = "ping", description = "인증 서버가 정상적으로 동작하는지 확인합니다.")
    @GetMapping("/ping")
    public CommonResponse<?> ping() {
        log.debug(">>> loginMemberId : {}", SecurityUtil.getLoginMemberId());
        return CommonResponse.ok("pong", null);
    }

    @Operation(summary = "SMS 인증 코드 전송", description = "사용자에게 인증 코드를 포함한 SMS를 전송합니다.")
    @PostMapping("/sms")
    public CommonResponse<String> sendSMS(
            @RequestBody SmsRequest smsRequest
    ) {
        authService.sendSms(smsRequest);
        return CommonResponse.ok("문자를 전송했습니다.", null);
    }

    @Operation(summary = "인증 코드 검증", description = "사용자가 입력한 SMS 인증 코드를 검증합니다.")
    @GetMapping("/verify_code")
    public CommonResponse<String> verifySMSCode(
            @RequestBody VerifyRequest verityRequest
    ) {
        Boolean result = authService.verifyCertificationCode(verityRequest.toCommand());

        // 메시지를 if 문 밖에서 선언하고 할당하도록 변경
        String message;
        if (result) {
            message = "인증에 성공했습니다.";
        } else {
            message = "인증에 실패했습니다.";
        }

        return CommonResponse.ok(message, null);
    }

    @Operation(summary = "회원 타입 조회", description = "회원의 타입을 조회합니다.")
    @GetMapping("/type")
    public CommonResponse<?> getMemberType() {
        MemberTypeResponse response = authService.getMemberType();
        return CommonResponse.ok(response);
    }

    @Operation(summary = "이메일 인증", description = "이메일을 통해 인증 코드를 전송합니다.")
    @PostMapping("/email")
    public CommonResponse<?> sendEmail(
            @RequestBody GmailSendRequest request
    ) {
        authService.sendEmail(request.toCommand());
        return CommonResponse.ok("이메일을 전송했습니다.", null);
    }
}
