package ssafy.ddada.api.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.member.request.MemberSignupRequest;
import ssafy.ddada.api.member.request.MemberUpdateRequest;
import ssafy.ddada.api.member.response.DeleteMemberResponse;
import ssafy.ddada.api.member.response.MemberDetailResponse;
import ssafy.ddada.api.member.response.MemberSignupResponse;
import ssafy.ddada.domain.member.player.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
@Tag(name = "Player", description = "회원관리")
public class MemberController {
    private final PlayerService playerService;

    @Operation(summary = "회원가입", description = """
         소셜로그인 시 저장된 임시 회원 정보를 정식 회원으로 업데이트하는 API입니다.
         이 과정을 통해 해당 회원은 임시 회원이 아닌 정식 회원으로 전환됩니다.
         """)
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<MemberSignupResponse> signup(
            @ModelAttribute @Validated MemberSignupRequest request
    ) {
        MemberSignupResponse response = playerService.signupMember(request.toCommand());

        return CommonResponse.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제하는 API입니다.")
    @DeleteMapping(value="")
    public CommonResponse<DeleteMemberResponse> deleteMember() {
        String message = playerService.deleteMember();
        return CommonResponse.ok(DeleteMemberResponse.of(message));
    }

    @Operation(summary = "닉네임 중복 조회", description = "닉네임 중복 조회하는 API입니다.")
    @GetMapping("/nickname")
    public CommonResponse<String> checkNickname(
            @RequestParam("nickname") String nickname
    ) {
        Boolean isDuplicated = playerService.checkNickname(nickname);
        if (isDuplicated) {
            String message = "이미 사용중인 닉네임입니다.";
            return CommonResponse.ok(message, null);
        } else {
            String message = "사용 가능한 닉네임입니다.";
            return CommonResponse.ok(message, null);
        }
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 API입니다.")
    @GetMapping("/profile")
    public CommonResponse<MemberDetailResponse> getMemberDetail(
    ) {
        MemberDetailResponse response = playerService.getMemberDetail();
        return CommonResponse.ok(response);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하는 API입니다.")
    @PutMapping(value = "/profile", consumes = { "multipart/form-data" })
    public CommonResponse<MemberDetailResponse> updateMemberProfile(
            @ModelAttribute @Validated MemberUpdateRequest request
    ) {
        MemberDetailResponse response = playerService.updateMemberProfile(request.toCommand());
        return CommonResponse.ok(response);
    }
}
