package ssafy.ddada.api.match;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.match.request.MatchCreateRequest;
import ssafy.ddada.api.match.request.MatchStatusChangeRequest;
import ssafy.ddada.api.match.request.TeamChangePlayerRequest;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.util.SecurityUtil;
import ssafy.ddada.domain.match.service.MatchService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matches")
@Tag(name = "Match", description = "경기관리")
public class MatchController {

    private final MatchService matchService;

    // TODO: ES 구상하기
    @Operation(summary = "경기 상태 기반 리스트 조회", description = "키워드 및 경기 상태 기반으로 경기 리스트를 조회하는 api입니다.")
    @GetMapping
    public CommonResponse<Page<MatchSimpleResponse>> getMatchesByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam String status,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        log.info("경기 검색 결과 조회 >>>> 검색어: {}, 경기 상태: {}, 페이지 번호: {}, 페이지 크기: {}", keyword, status, page, size);

        Page<MatchSimpleResponse> response = matchService.getMatchesByKeyword(keyword, status, page, size);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "경기 세부 조회", description = "경기 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{match_id}")
    public CommonResponse<MatchDetailResponse> getMatchById(@PathVariable("match_id") Long matchId) {
        log.info("경기 세부 정보 조회 >>>> 경기 ID: {}", matchId);

        MatchDetailResponse response = matchService.getMatchById(matchId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "세트 세부 조회", description = "세트 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{match_id}/sets/{set_number}")
    public CommonResponse<SetDetailResponse> getSetById(@PathVariable("match_id") Long matchId, @PathVariable("set_number") Integer setNumber) {
        log.info("세트 세부 조회 >>>> 경기 ID: {}, 세트 ID: {}", matchId, setNumber);

        SetDetailResponse response = matchService.getSetById(matchId, setNumber);
        return CommonResponse.ok(response);
    }

//    TODO: login 해야만 볼 수 있도록 Filter 추가하기
    @Operation(summary = "경기 생성", description = "경기를 생성하는 api입니다.")
    @PostMapping
    public CommonResponse<MatchDetailResponse> createMatch(@RequestBody MatchCreateRequest request) {
        Long creatorId = SecurityUtil.getLoginMemberId();
        log.info("경기 생성 >>>> 생성인 ID: {}, 경기 정보: {}", creatorId, request);

        matchService.createMatch(creatorId, request.toCommand());
        return CommonResponse.created("경기가 성공적으로 생성되었습니다.", null);
    }

    @Operation(summary = "경기 상태 전환", description = "경기 상태를 전환하는 api입니다.")
    @PatchMapping("/status")
    public CommonResponse<?> updateMatchStatus(@RequestBody MatchStatusChangeRequest request){
        log.info("경기 상태 전환 >>>> 경기 번호: {}, 경기 상태: {}", request.matchId(), request.status());

        matchService.updateMatchStatus(request.toCommand());
        return CommonResponse.ok("경기 상태가 성공적으로 전환되었습니다.", null);
    }

    @Deprecated
    @Operation(summary = "팀 세부 조회", description = "팀 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{match_id}/teams/{team_number}")
    public CommonResponse<TeamDetailResponse> getTeamById(@PathVariable("match_id") Long matchId, @PathVariable("team_number") Integer teamNumber) {
        log.info("팀 세부 조회 >>>> 경기 ID: {}, 팀 번호: {}", matchId, teamNumber);

        TeamDetailResponse response = matchService.getTeamByTeamNumber(matchId, teamNumber);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "팀 선수 변경", description = "팀 선수를 변경하는 api입니다.")
    @PatchMapping("/{match_id}/teams")
    public CommonResponse<?> updateTeamPlayer(@PathVariable("match_id") Long matchId, @RequestBody TeamChangePlayerRequest request){
        log.info("팀 선수 변경 >>> 경기 ID: {}, 요청: {}", matchId, request);

        matchService.updateTeamPlayer(matchId, request.toCommand());
        return CommonResponse.ok("팀 선수가 성공적으로 변경되었습니다.", null);
    }


    @Operation(summary = "매니저 경기 할당", description = "매니저에 경기를 할당하는 api입니다.")
    @PatchMapping("/matches/{match_id}")
    public CommonResponse<?> allocateToMatch(@PathVariable("match_id") Long matchId){
        Long managerId = SecurityUtil.getLoginMemberId();
        log.info("매니저 경기 할당 >>>> 매니저 ID: {}, 경기 ID: {}", managerId, matchId);

        matchService.allocateManager(matchId, managerId);
        return CommonResponse.ok("경기에 성공적으로 할당되었습니다.", null);
    }

}
