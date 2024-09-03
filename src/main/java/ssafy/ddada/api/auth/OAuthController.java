package ssafy.ddada.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ssafy.ddada.common.properties.KakaoLoginProperties;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {
    private final KakaoLoginProperties kakaoLoginProperties;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 페이지로 이동합니다.")
    @GetMapping("/kakao/login")
    public String login() {
        return "redirect:" + "https://kauth.kakao.com/oauth/authorize?client_id=" +kakaoLoginProperties.clientId()
                + "&response_type=code&redirect_uri=" +kakaoLoginProperties.loginRedirectUri()
                /*+"&prompt=login"*/;
    }

    @Operation(summary = "카카오 로그아웃", description = "카카오 로그아웃 페이지로 이동합니다.")
    @GetMapping("/kakao/logout")
    public String logout() {
        return "redirect:" + "https://kauth.kakao.com/oauth/logout?client_id=" +kakaoLoginProperties.clientId()
                + "&logout_redirect_uri=" +kakaoLoginProperties.logoutRedirectUri();
    }
}
