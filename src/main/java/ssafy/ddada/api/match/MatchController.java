package ssafy.ddada.api.match;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.match.request.MatchCreateRequest;
import ssafy.ddada.api.match.request.MatchStatusChangeRequest;
import ssafy.ddada.api.match.request.TeamCreateRequest;
import ssafy.ddada.api.match.response.MatchCreateResponse;
import ssafy.ddada.api.match.response.MatchDetailResponse;
import ssafy.ddada.api.match.response.MatchStatusChangeResponse;
import ssafy.ddada.api.match.response.TeamCreateResponse;
import ssafy.ddada.domain.match.service.MatchService;
import ssafy.ddada.domain.match.service.TeamService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;
    private final TeamService teamService;

    // TODO: ES 구상하기
    @GetMapping
    public MatchDetailResponse getMatches(@RequestParam(required = false) String keyword) {
        return null;
    }

    @Operation(summary = "경기 세부 조회", description = "경기 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{matchId}")
    public MatchDetailResponse getMatch(@PathVariable Long matchId) {
        log.info("경기 세부 정보 조회 >>>> 경기 ID: {}", matchId);
        return matchService.getMatchById(matchId);
    }

    @Operation(summary = "경기 생성", description = "경기를 생성하는 api입니다.")
    @PostMapping
    public MatchCreateResponse createMatch(@RequestBody MatchCreateRequest request) {
        log.info("경기 생성 >>>> 경기 정보: {}", request);
        return matchService.createMatch(request.toCommand());
    }

    @Operation(summary = "경기 상태 전환", description = "경기 상태를 전환하는 api입니다.")
    @PatchMapping("/status")
    public MatchStatusChangeResponse changeMatchStatus(@RequestBody MatchStatusChangeRequest request){
        log.info("경기 상태 전환 >>>> 경기 번호: {}, 경기 상태: {}", request.matchId(), request.status());
        return matchService.changeMatchStatus(request.toCommand());
    }

    @Operation(summary = "팀 생성", description = "팀을 생성하는 api입니다.")
    @PostMapping("/team")
    public TeamCreateResponse createTeam(@RequestBody TeamCreateRequest request){
        log.info("팀 생성 >>>> 선수1 ID: {}, 선수2 ID: {}", request.player1_id(), request.player2_id());
        return teamService.createTeam(request.toCommand());
    }

}
