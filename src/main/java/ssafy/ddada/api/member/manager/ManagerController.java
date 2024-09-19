package ssafy.ddada.api.member.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.member.manager.request.ManagerSearchMatchRequest;
import ssafy.ddada.api.member.manager.request.ManagerSignupRequest;
import ssafy.ddada.api.member.manager.response.ManagerDetailResponse;
import ssafy.ddada.api.match.request.MatchResultRequest;
import ssafy.ddada.api.match.response.MatchSimpleResponse;
import ssafy.ddada.api.member.manager.response.ManagerSignupResponse;
import ssafy.ddada.common.exception.Exception.Security.NotAuthenticatedException;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.manager.service.ManagerService;
import ssafy.ddada.domain.match.service.MatchService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
@Tag(name = "Manager", description = "매니저관리")
public class ManagerController {
//TODO: 진입 전에 Manager 로그인 여부 판단하는 필터 적용하기
    private final ManagerService managerService;
    private final MatchService matchService;

    @Operation(summary = "매니저 세부 조회", description = "매니저 세부 정보를 조회하는 api입니다.")
    @GetMapping
    public CommonResponse<ManagerDetailResponse> getManager(){
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("매니저 세부 조회 >>>> 매니저 ID: {}", managerId);

        ManagerDetailResponse response = managerService.getManagerById(managerId);
        return CommonResponse.ok(response);
    }

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

    @Operation(summary = "매니저 경기 할당", description = "매니저에 경기를 할당하는 api입니다.")
    @PatchMapping("/matches/{match_id}")
    public CommonResponse<?> allocateToMatch(@PathVariable("match_id") Long matchId){
        MemberRole memberRole = SecurityUtil.getLoginMemberRole()
                .orElseThrow(NotAuthenticatedException::new);
        if (memberRole != MemberRole.MANAGER){
            throw new NotAuthenticatedException();
        }

        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("매니저 경기 할당 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

        matchService.allocateManager(matchId, managerId);
        return CommonResponse.ok("경기에 성공적으로 할당되었습니다.", null);
    }

    @Operation(summary = "할당된 경기 저장", description = "경기 종료 후 경기 데이터를 저장하는 api입니다.")
    @PutMapping("/matches/{match_id}")
    public CommonResponse<?> saveMatch(@PathVariable("match_id") Long matchId, @RequestBody MatchResultRequest request){
//        TODO: 이건 매니저 검사용으로만 쓰기. managerId는 비즈니스 로직상 안씀
        Long managerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("할당된 경기 저장 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

//        TODO: 경기 Request 받아 저장 기능 만들기
        matchService.saveMatch(matchId, request.toCommand());
        return CommonResponse.ok("저장되었습니다.", null);
    }

    @Operation(summary = "매니저 회원가입", description = "매니저 회원가입 api입니다.")
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public CommonResponse<?> signup(
            @ModelAttribute @Validated ManagerSignupRequest request
    ) {
        ManagerSignupResponse response = managerService.signupManager(request.toCommand());

        return CommonResponse.ok(response);
    }
}
