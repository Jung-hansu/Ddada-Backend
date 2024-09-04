package ssafy.ddada.api.member;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.member.request.MemberLoginRequest;
import ssafy.ddada.api.member.request.MemberSignupRequest;
import ssafy.ddada.api.member.response.DeleteMemberResponse;
import ssafy.ddada.api.member.response.MemberLoginResponse;
import ssafy.ddada.api.member.response.MemberSignupResponse;
import ssafy.ddada.domain.member.service.MemberService;
import ssafy.ddada.domain.member.command.MemberSignupCommand;


import static ssafy.ddada.api.member.MemberResponseMessages.SIGNED_UP_MEMBER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = """
         소셜로그인 시 저장된 임시 회원 정보를 정식 회원으로 업데이트하는 API입니다.
         이 과정을 통해 해당 회원은 임시 회원이 아닌 정식 회원으로 전환됩니다.
         """)
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<MemberSignupResponse> signup(
            @Valid @RequestBody MemberSignupRequest request
    ) {
        MemberSignupCommand signupCommand = request.toCommand(null);

        String message = memberService.signupMember(signupCommand, request.getProfileImagePath());
        return CommonResponse.ok(MemberSignupResponse.of(message));
    }

//    @Operation(summary = "회원 로그인", description = "회원 로그인을 하는 API입니다.")
//    @PostMapping(value = "/login")
//    public CommonResponse<?> login(@Valid @RequestBody MemberLoginRequest request) {
//        MemberLoginResponse response = memberService.loginMember(request.toCommand());
//        return CommonResponse.ok(response);
//    }

    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제하는 API입니다.")
    @DeleteMapping(value="")
    public CommonResponse<DeleteMemberResponse> deleteMember() {
        String message = memberService.deleteMember();
        return CommonResponse.ok(DeleteMemberResponse.of(message));
    }
}
