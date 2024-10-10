package ssafy.ddada.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.auth.request.*;
import ssafy.ddada.api.auth.response.AuthResponse;
import ssafy.ddada.api.auth.response.MemberTypeResponse;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.api.auth.request.TokenRefreshRequest;
import ssafy.ddada.domain.auth.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인", description = "유저 정보를 이용하여 login type으로 로그인 합니다.")
    @PostMapping("/login")
    public CommonResponse<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("[AuthController] 로그인 >>>> 로그인 타입: {}, 이메일: {}", request.loginType(), request.email());
        AuthResponse loginResponse = authService.login(request.toCommand());
        return CommonResponse.ok(loginResponse);
    }

    @Operation(summary = "토큰 갱신", description = "refresh token으로 access token을 갱신합니다.")
    @PostMapping("/refresh")
    public CommonResponse<AuthResponse> refresh(@RequestBody TokenRefreshRequest request) {
        log.info("[AuthController] Access 토큰 갱신");
        AuthResponse rotateTokenResponse = authService.refresh(request);
        return CommonResponse.ok(rotateTokenResponse);
    }

    @Operation(summary = "로그아웃", description = "엑세스 토큰을 이용하여 login type으로 로그인 합니다.")
    @PostMapping("/logout")
    public CommonResponse<?> logout() {
        log.info("[AuthController] 로그아웃");
        authService.logout();
        return CommonResponse.noContent();
    }

    @Operation(summary = "ping", description = "인증 서버가 정상적으로 동작하는지 확인합니다.")
    @GetMapping("/ping")
    public CommonResponse<?> ping() {
        log.info("[AuthController] 인증 확인 >>>> 로그인 멤버 ID : {}", SecurityUtil.getLoginMemberId());
        return CommonResponse.ok("pong", null);
    }

    @Operation(summary = "SMS 인증 코드 전송", description = "사용자에게 인증 코드를 포함한 SMS를 전송합니다.")
    @PostMapping("/sms")
    public CommonResponse<String> sendSMS(@RequestBody SmsRequest smsRequest) {
        log.info("[AuthController] SMS 인증 코드 전송 >>>> 전화번호: {}", smsRequest.phoneNum());
        authService.sendSms(smsRequest.toCommand());
        return CommonResponse.ok("문자를 전송했습니다.", null);
    }

    @Operation(summary = "인증 코드 검증", description = "사용자가 입력한 SMS 인증 코드를 검증합니다.")
    @PostMapping("/verify_code")
    public CommonResponse<String> verifySMSCode(@RequestBody VerifyRequest verityRequest) {
        log.info("[AuthController] SMS 인증 코드 검증 >>>> 사용자 정보: {}", verityRequest.userInfo());
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
        log.info("[AuthController] 회원 타입 조회");
        MemberTypeResponse response = authService.getMemberType();
        return CommonResponse.ok(response);
    }

    @Operation(summary = "이메일 인증", description = "이메일을 통해 인증 코드를 전송합니다.")
    @PostMapping("/email")
    public CommonResponse<?> sendEmail(@RequestBody GmailSendRequest request) {
        log.info("[AuthController] 이메일 인증 코드 전송 >>>> 이메일: {}", request.email());
        authService.sendEmail(request.toCommand());
        return CommonResponse.ok("이메일을 전송했습니다.", null);
    }

}
