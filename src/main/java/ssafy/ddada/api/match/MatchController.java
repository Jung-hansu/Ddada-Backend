package ssafy.ddada.api.match;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.match.request.CheckPlayerBookedRequest;
import ssafy.ddada.api.match.request.MatchCreateRequest;
import ssafy.ddada.api.match.request.MatchSearchRequest;
import ssafy.ddada.api.match.response.*;
import ssafy.ddada.domain.match.service.MatchService;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matches")
@Tag(name = "Match", description = "경기관리")
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "경기 리스트 조회", description = "키워드 및 경기 상태 기반으로 경기 리스트를 조회하는 api입니다.")
    @GetMapping
    public CommonResponse<Page<MatchSimpleResponse>> getMatchesByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "NORMAL") String rankType,
            @RequestParam(required = false) String matchTypes,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String regions,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("[MatchController] 경기 검색 결과 조회 >>>> 검색어: {}, 랭크 타입: {}, 경기 타입: {}, 경기 상태: {}, 페이지 번호: {}, 페이지 크기: {}", keyword, rankType, matchTypes, statuses, page, size);
        MatchSearchRequest request = new MatchSearchRequest(keyword, rankType, matchTypes, statuses, regions, page, size);
        Page<MatchSimpleResponse> response = matchService.getFilteredMatches(request.toCommand());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "경기 세부 조회", description = "경기 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{match_id}")
    public CommonResponse<MatchDetailResponse> getMatchById(@PathVariable("match_id") Long matchId) {
        log.info("[MatchController] 경기 세부 정보 조회 >>>> 경기 ID: {}", matchId);
        MatchDetailResponse response = matchService.getMatchByIdWithInfos(matchId);
        return CommonResponse.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "경기 생성", description = "경기를 생성하는 api입니다.")
    @PostMapping
    public CommonResponse<MatchDetailResponse> createMatch(@RequestBody MatchCreateRequest request) {
        log.info("[MatchController] 경기 생성 >>>> 경기 ID: {}, 랭크 타입: {}, 경기 타입: {}, 경기 일자: {}, 경기 시간: {}", request.courtId(), request.rankType(), request.matchType(), request.date(), request.time());
        matchService.createMatch(request.toCommand());
        return CommonResponse.created("경기가 성공적으로 생성되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "팀 선수 추가", description = "팀 선수를 추가하는 api입니다.")
    @PatchMapping("/{match_id}/teams/{team_number}")
    public CommonResponse<?> setTeamPlayer(@PathVariable("match_id") Long matchId, @PathVariable("team_number") Integer teamNumber){
        log.info("[MatchController] 팀 선수 추가 >>>> 경기 ID: {}, 팀 번호: {}", matchId, teamNumber);
        matchService.setTeamPlayer(matchId, teamNumber);
        return CommonResponse.ok("팀 선수가 성공적으로 변경되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "팀 선수 제거", description = "팀 선수를 제거하는 api입니다.")
    @DeleteMapping("/{match_id}/teams/{team_number}")
    public CommonResponse<?> unsetTeamPlayer(@PathVariable("match_id") Long matchId, @PathVariable("team_number") Integer teamNumber){
        log.info("[MatchController] 팀 선수 제거 >>>> 경기 ID: {}, 팀 번호: {}", matchId, teamNumber);
        matchService.unsetTeamPlayer(matchId, teamNumber);
        return CommonResponse.ok("팀 선수가 성공적으로 변경되었습니다.", null);
    }

    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @Operation(summary = "선수 예약된 경기 확인", description = "같은 시간에 이미 예약된 경기가 있는지 확인하는 api입니다.")
    @GetMapping("/player/bookings")
    public CommonResponse<Boolean> checkPlayerBooked(@RequestParam LocalDate date, @RequestParam LocalTime time) {
        log.info("[MatchController] 선수 예약 여부 확인 >>>> 날짜: {}, 시간: {}",date, time);
        CheckPlayerBookedRequest request = new CheckPlayerBookedRequest(date, time);
        Boolean response = matchService.CheckPlayerBooked(request.toCommand());
        return CommonResponse.ok(response);
    }
}
