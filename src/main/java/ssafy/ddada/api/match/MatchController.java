package ssafy.ddada.api.match;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.match.request.MatchCreateRequest;
import ssafy.ddada.api.match.request.MatchSearchRequest;
import ssafy.ddada.api.match.request.MatchStatusChangeRequest;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.common.exception.Exception.Security.NotAuthenticatedException;
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
            @RequestParam(defaultValue = "친선") String rankType,
            @RequestParam(required = false) String matchTypes,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String regions,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Long memberId = SecurityUtil.getLoginMemberId().orElse(null);
        MatchSearchRequest request = new MatchSearchRequest(keyword, rankType, matchTypes, statuses, regions, page, size);
        log.info("경기 검색 결과 조회 >>>> 멤버 ID: {}, 검색어: {}, 랭크 타입: {}, 경기 타입: {}, 경기 상태: {}, 페이지 번호: {}, 페이지 크기: {}", memberId, keyword, rankType, matchTypes, statuses, page, size);

        Page<MatchSimpleResponse> response = matchService.getFilteredMatches(memberId, request.toCommand());
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
        Long creatorId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
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

    @Operation(summary = "팀 선수 추가", description = "팀 선수를 추가하는 api입니다.")
    @PatchMapping("/{match_id}/teams/{team_number}")
    public CommonResponse<?> setTeamPlayer(@PathVariable("match_id") Long matchId, @PathVariable("team_number") Integer teamNumber){
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("팀 선수 변경 >>> 경기 ID: {}, 선수 ID: {}, 팀 번호: {}", matchId, playerId, teamNumber);

        matchService.setTeamPlayer(matchId, playerId, teamNumber);
        return CommonResponse.ok("팀 선수가 성공적으로 변경되었습니다.", null);
    }

    @Operation(summary = "팀 선수 제거", description = "팀 선수를 제거하는 api입니다.")
    @DeleteMapping("/{match_id}/teams/{team_number}")
    public CommonResponse<?> unsetTeamPlayer(@PathVariable("match_id") Long matchId, @PathVariable("team_number") Integer teamNumber){
        Long playerId = SecurityUtil.getLoginMemberId()
                .orElseThrow(NotAuthenticatedException::new);
        log.info("팀 선수 변경 >>> 경기 ID: {}, 선수 ID: {}, 팀 번호: {}", matchId, playerId, teamNumber);

        matchService.unsetTeamPlayer(matchId, playerId, teamNumber);
        return CommonResponse.ok("팀 선수가 성공적으로 변경되었습니다.", null);
    }

}
