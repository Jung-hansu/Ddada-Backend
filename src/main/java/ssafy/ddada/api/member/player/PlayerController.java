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
import ssafy.ddada.api.member.player.response.*;
import ssafy.ddada.domain.member.player.service.PlayerService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
@Tag(name = "Player", description = "회원관리")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "회원가입", description = """
         소셜로그인 시 저장된 임시 회원 정보를 정식 회원으로 업데이트하는 API입니다.
         이 과정을 통해 해당 회원은 임시 회원이 아닌 정식 회원으로 전환됩니다.
     """)
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<PlayerSignupResponse> signup(@ModelAttribute @Validated PlayerSignupRequest request) {
        log.info("[PlayerController] 회원가입 >>>> request: {}", request);
        PlayerSignupResponse response = playerService.signupMember(request.toCommand());
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제하는 API입니다.")
    @PatchMapping
    public CommonResponse<?> deleteMember() {
        log.info("[PlayerController] 회원 탈퇴");
        String message = playerService.deleteMember();
        return CommonResponse.ok(message, null);
    }

    @Operation(summary = "닉네임 중복 조회", description = "닉네임 중복 조회하는 API입니다.")
    @GetMapping("/nickname")
    public CommonResponse<String> checkNickname(@RequestParam String nickname) {
        log.info("[PlayerController] 닉네임 중복 조회 >>>> nickname: {}", nickname);
        boolean isDuplicated = playerService.checkNickname(nickname);
        String message = isDuplicated ? "이미 사용중인 닉네임입니다." : "사용 가능한 닉네임입니다.";
        return CommonResponse.ok(message, null);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "로그인 시 회원 정보 조회", description = "로그인 시 회원 정보를 조회하는 API입니다.")
    @GetMapping
    public CommonResponse<PlayerDetailResponse> getMemberDetail() {
        log.info("[PlayerController] 로그인 시 회원 정보 조회");
        PlayerDetailResponse response = playerService.getMemberDetail();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "프로필에서 회원 정보 조회", description = "프로필에서 회원 정보를 조회하는 API입니다.")
    @GetMapping("/profile")
    public CommonResponse<PlayerProfileDetailResponse> getMemberProfileDetail() {
        log.info("[PlayerController] 프로필에서 회원 정보 조회");
        PlayerProfileDetailResponse response = playerService.getMemberProfileDetail();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하는 API입니다.")
    @PutMapping(value = "/profile", consumes = { "multipart/form-data" })
    public CommonResponse<PlayerDetailResponse> updateMemberProfile(@ModelAttribute @Validated PlayerUpdateRequest request) {
        log.info("[PlayerController] 회원 정보 수정 >>>> request: {}", request);
        PlayerDetailResponse response = playerService.updateMemberProfile(request.toCommand());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정하는 API입니다.")
    @PatchMapping(value = "/password")
    public CommonResponse<String> updateMemberPassword(@RequestBody PasswordUpdateRequest request) {
        log.info("[PlayerController] 회원 비밀번호 수정 >>>> request: {}", request);
        playerService.updateMemberPassword(request.toCommand());
        return CommonResponse.ok("비밀번호가 성공적으로 변경되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 경기 조회", description = "플레이어의 경기들을 조회하는 API입니다.")
    @GetMapping("/matches")
    public CommonResponse<List<PlayerMatchResponse>> getPlayerMatches() {
        log.info("[PlayerController] 플레이어의 경기 조회");
        List<PlayerMatchResponse> response = playerService.getPlayerMatches();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 완료된 경기 조회", description = "플레이어의 완료된 경기들을 조회하는 API입니다.")
    @GetMapping("/matches/finished")
    public CommonResponse<List<PlayerMatchResponse>> getPlayerCompleteMatches() {
        log.info("[PlayerController] 플레이어의 완료된 경기 조회");
        List<PlayerMatchResponse> response = playerService.getPlayerCompleteMatches();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 ID 조회", description = "나의 id를 조회하는 API입니다.")
    @GetMapping("/id")
    public CommonResponse<PlayerIdResponse> getPlayerId() {
        log.info("[PlayerController] 플레이어의 ID 조회");
        PlayerIdResponse response = playerService.getPlayerId();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어의 총 경기 수 조회", description = "플레이어의 총 경기 수를 조회하는 API입니다.")
    @GetMapping("/matches/total")
    public CommonResponse<PlayerTotalMatchResponse> getPlayerTotalMatch() {
        PlayerTotalMatchResponse response = playerService.getPlayerTotalMatch();
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "플레이어들의 랭킹 조회", description = "플레이어들의 랭킹을 조회하는 API입니다.")
    @GetMapping("/rankings")
    public CommonResponse<List<PlayerRankingResponse>> getPlayersRanking() {
        List<PlayerRankingResponse> response = playerService.getPlayersRanking();
        return CommonResponse.ok(response);
    }
}
