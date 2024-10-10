package ssafy.ddada.api.data;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.data.request.RacketRecommendRequest;
import ssafy.ddada.api.data.response.PlayerAnalysticResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.api.data.response.RacketRecommendResponse;
import ssafy.ddada.domain.data.service.DataService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @Operation(summary = "선수 경기 데이터 분석 조회", description = "선수의 경기 데이터 분석을 조회하는 API입니다.")
    @GetMapping("/player/{match_id}")
    public CommonResponse<PlayerMatchAnalyticsResponse> PlayerMatchAnalytics(@PathVariable("match_id") Long matchId) {
        log.info("[DataController] 선수 경기 데이터 분석 조회 >>>> 경기 ID: {}", matchId);
        PlayerMatchAnalyticsResponse response = dataService.PlayerMatchAnalytics(matchId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "선수 분석 조회", description = "선수의 분석 데이터를 조회하는 API입니다.")
    @GetMapping("/player")
    public CommonResponse<PlayerAnalysticResponse> PlayerAnalytics() {
        log.info("[DataController] 선수 분석 조회");
        PlayerAnalysticResponse response = dataService.PlayerAnalytics();
        return CommonResponse.ok(response);
    }

    @Operation(summary = "추천 라켓 조회", description = "추천 라켓을 조회하는 API입니다")
    @GetMapping("/rackets")
    public CommonResponse<RacketRecommendResponse> ReccommandRacket(
            @RequestParam(value = "balance") String balance,
            @RequestParam(value = "weight") String weight,
            @RequestParam(value = "shaft") String shaft,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "racket_id", required = false) List<Integer> racketIds
    ) {
        log.info("[DataController] 추천 라켓 조회 >>>> 밸런스: {}, 무게: {}, 샤프트: {}, 가격: {}, 라켓 IDs: {}", balance, weight, shaft, price, racketIds);
        RacketRecommendRequest request = new RacketRecommendRequest(balance, weight, shaft, price, racketIds);
        RacketRecommendResponse response = dataService.RecommendRacket(request.toCommand());
        return CommonResponse.ok(response);
    }

}
