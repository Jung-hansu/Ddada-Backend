package ssafy.ddada.api.member.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.member.player.request.PasswordUpdateRequest;
import ssafy.ddada.api.member.player.request.PlayerSignupRequest;
import ssafy.ddada.api.member.player.request.PlayerUpdateRequest;
import ssafy.ddada.api.member.player.response.PlayerDeleteResponse;
import ssafy.ddada.api.member.player.response.PlayerDetailResponse;
import ssafy.ddada.api.member.player.response.PlayerMatchResponse;
import ssafy.ddada.api.member.player.response.PlayerSignupResponse;
import ssafy.ddada.domain.member.player.service.PlayerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
@Slf4j
@Tag(name = "Player", description = "회원관리")
public class PlayerController {
    private final PlayerService playerService;

    @Operation(summary = "회원가입", description = """
         소셜로그인 시 저장된 임시 회원 정보를 정식 회원으로 업데이트하는 API입니다.
         이 과정을 통해 해당 회원은 임시 회원이 아닌 정식 회원으로 전환됩니다.
         """)
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<PlayerSignupResponse> signup(
            @ModelAttribute @Validated PlayerSignupRequest request
    ) {
        PlayerSignupResponse response = playerService.signupMember(request.toCommand());

        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제하는 API입니다.")
    @PatchMapping(value="")
    public CommonResponse<PlayerDeleteResponse> deleteMember() {
        String message = playerService.deleteMember();
        return CommonResponse.ok(PlayerDeleteResponse.of(message));
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

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 API입니다.")
    @GetMapping("/profile")
    public CommonResponse<PlayerDetailResponse> getMemberDetail(
    ) {
        PlayerDetailResponse response = playerService.getMemberDetail();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하는 API입니다.")
    @PutMapping(value = "/profile", consumes = { "multipart/form-data" })
    public CommonResponse<PlayerDetailResponse> updateMemberProfile(
            @ModelAttribute @Validated PlayerUpdateRequest request
    ) {
        PlayerDetailResponse response = playerService.updateMemberProfile(request.toCommand());
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정하는 API입니다.")
    @PatchMapping(value = "/password")
    public CommonResponse<String> updateMemberPassword(
            @RequestBody PasswordUpdateRequest request
            ) {
        String response = playerService.updateMemberPassword(request.toCommand());
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 경기 조회", description = "플레이어의 경기들을 조회하는 API입니다.")
    @GetMapping("/matches")
    public CommonResponse<List<PlayerMatchResponse>> getPlayerMatches(
    ) {
        List<PlayerMatchResponse> response = playerService.getPlayerMatches();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 완료된 경기 조회", description = "플레이어의 완료된 경기들을 조회하는 API입니다.")
    @GetMapping("/matches/complete")
    public CommonResponse<List<PlayerMatchResponse>> getPlayerCompleteMatches(
    ) {
        List<PlayerMatchResponse> response = playerService.getPlayerCompleteMatches();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 id 조회", description = "나의 id를 조회하는 API입니다.")
    @GetMapping("/id")
    public CommonResponse<Long> getPlayerId(
    ) {
        Long response = playerService.getPlayerId();
        return CommonResponse.ok(response);
    }
}
