package ssafy.ddada.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.auth.request.LoginRequest;
import ssafy.ddada.api.auth.request.LogoutRequest;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.config.auth.AuthResponse;
import ssafy.ddada.config.auth.IdToken;
import ssafy.ddada.config.auth.TokenRefreshRequest;
import ssafy.ddada.domain.auth.Service.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인 v3", description = "유저 정보를 이용하여 login type으로 로그인 합니다.")
    @PostMapping("/login")
    public CommonResponse<AuthResponse> loginv2(
            @RequestBody LoginRequest request
    ) {
        AuthResponse loginResponse = authService.loginv2(request.authcode());
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

    @Operation(summary = "로그아웃 v3", description = "엑세스 토큰을 이용하여 login type으로 로그인 합니다.")
    @PostMapping("/{loginType}/logout")
    public CommonResponse logoutv2(
            @Schema(description = "로그인 타입", allowableValues = {"kakao"}, example = "kakao")
            @PathVariable("loginType") String loginType,
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request.accessToken());
        return CommonResponse.noContent();
    }

    @Operation(summary = "ping", description = "인증 서버가 정상적으로 동작하는지 확인합니다.")
    @GetMapping("/ping")
    public CommonResponse ping() {
        log.debug(">>> loginMemberId : {}", SecurityUtil.getLoginMemberId());
        return CommonResponse.ok("pong");
    }

    @Deprecated
    @Operation(summary = "id token 발급", description = "인가 코드로 id token을 발급받습니다.")
    @GetMapping("/{loginType}/id-token")
    public CommonResponse<IdToken> getIdToken(
            @Schema(description = "로그인 타입", allowableValues = {"kakao"}, example = "kakao")
            @PathVariable("loginType") String loginType,

            @RequestParam("code") String code
    ) {
        IdToken idToken = authService.getIdToken(loginType, code);
        return CommonResponse.ok(idToken);
    }

    @Operation(summary = "닉네임 중복 조회", description = "닉네임 중복 조회하는 API입니다.")
    @GetMapping("/nickname")
    public CommonResponse<String> checkNickname(
            @RequestParam("nickname") String nickname
    ) {
        Boolean isDuplicated = authService.checkNickname(nickname);
        if (isDuplicated) {
            String message = "이미 사용중인 닉네임입니다.";
            return CommonResponse.ok(message);
        } else {
            String message = "사용 가능한 닉네임입니다.";
            return CommonResponse.ok(message);
        }
    }
}
