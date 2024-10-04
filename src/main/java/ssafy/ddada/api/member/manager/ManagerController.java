package ssafy.ddada.api.member.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.member.manager.request.ManagerMatchStatusChangeRequest;
import ssafy.ddada.api.member.manager.request.ManagerSearchMatchRequest;
import ssafy.ddada.api.member.manager.request.ManagerSignupRequest;
import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.api.match.request.MatchResultRequest;
import ssafy.ddada.api.match.response.MatchSimpleResponse;
import ssafy.ddada.api.member.manager.response.ManagerIdResponse;
import ssafy.ddada.api.member.manager.response.ManagerSignupResponse;
import ssafy.ddada.common.exception.security.NotAuthenticatedException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.manager.service.ManagerService;
import ssafy.ddada.domain.match.service.MatchService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
@Tag(name = "Manager", description = "매니저관리")
public class ManagerController {
    private final ManagerService managerService;
    private final MatchService matchService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "매니저 세부 조회", description = "매니저 세부 정보를 조회하는 api입니다.")
    @GetMapping
    public CommonResponse<ManagerDetailResponse> getManager(){
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("매니저 세부 조회 >>>> 매니저 ID: {}", managerId);

        ManagerDetailResponse response = managerService.getManagerById(managerId);
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "매니저 할당 경기 리스트 조회", description = "매니저에게 할당된 경기 리스트를 조회하는 api입니다.")
    @GetMapping("/matches")
    public CommonResponse<Page<MatchSimpleResponse>> getAllocatedMatches(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") Boolean todayOnly,
            @RequestParam String statuses,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        ManagerSearchMatchRequest request = new ManagerSearchMatchRequest(managerId, keyword, todayOnly, statuses, page, size);
        log.info("매니저 경기 리스트 조회 >>>> 매니저 ID: {}", managerId);

        Page<MatchSimpleResponse> response = matchService.getMatchesByManagerId(request.toCommand());
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "매니저 경기 할당", description = "매니저에 경기를 할당하는 api입니다.")
    @PatchMapping("/matches/{match_id}")
    public CommonResponse<?> allocateToMatch(@PathVariable("match_id") Long matchId){

        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("매니저 경기 할당 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

        matchService.allocateManager(matchId, managerId);
        return CommonResponse.ok("경기에 성공적으로 할당되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "매니저 경기 할당 해제", description = "매니저를 경기에서 할당 해제하는 api입니다.")
    @DeleteMapping("/matches/{match_id}")
    public CommonResponse<?> deallocateToMatch(@PathVariable("match_id") Long matchId){
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("매니저 경기 할당 해제 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

        matchService.deallocateManager(matchId, managerId);
        return CommonResponse.noContent();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "할당된 경기 저장", description = "경기 종료 후 경기 데이터를 저장하는 api입니다.")
    @PutMapping("/matches/{match_id}")
    public CommonResponse<?> saveMatch(@PathVariable("match_id") Long matchId, @RequestBody MatchResultRequest request){
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("할당된 경기 저장 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

        matchService.saveMatch(matchId, managerId, request.toCommand());
        return CommonResponse.ok("저장되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "경기 상태 전환", description = "경기 상태를 전환하는 api입니다.")
    @PatchMapping("/matches/{match_id}/status")
    public CommonResponse<?> updateMatchStatus(
            @PathVariable("match_id") Long matchId,
            @RequestBody ManagerMatchStatusChangeRequest request
    ){
        log.info("경기 상태 전환 >>>> 경기 번호: {}, 경기 상태: {}", matchId, request);

        matchService.updateMatchStatus(matchId, request.toCommand());
        return CommonResponse.ok("경기 상태가 성공적으로 전환되었습니다.", request.toCommand());
    }

    @Operation(summary = "매니저 회원가입", description = "매니저 회원가입 api입니다.")
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<?> signup(
            @ModelAttribute @Validated ManagerSignupRequest request
    ) {
        ManagerSignupResponse response = managerService.signupManager(request.toCommand());

        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "매니저 정보 조회", description = "매니저 정보를 조회하는 API입니다.")
    @GetMapping("/id")
    public CommonResponse<ManagerIdResponse> getManagerId(
    ) {
        ManagerIdResponse response = managerService.getManagerId();
        return CommonResponse.ok(response);
    }
}
