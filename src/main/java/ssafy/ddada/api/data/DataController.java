package ssafy.ddada.api.data;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.data.request.RacketRecommendRequest;
import ssafy.ddada.api.data.response.PlayerAnalysticResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.api.data.response.RacketRecommendResponse;
import ssafy.ddada.domain.data.service.DataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @Operation(summary = "선수 경기 데이터 분석 조회", description = "선수의 경기 데이터 분석을 조회하는 API입니다.")
    @GetMapping("/player/{match_id}")
    public CommonResponse<PlayerMatchAnalyticsResponse> PlayerMatchAnalytics(@PathVariable("match_id") Long matchId) {
        PlayerMatchAnalyticsResponse response = dataService.PlayerMatchAnalytics(matchId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "선수 분석 조회", description = "선수의 분석 데이터를 조회하는 API입니다.")
    @GetMapping("/player")
    public CommonResponse<PlayerAnalysticResponse> PlayerAnalytics() {
        PlayerAnalysticResponse response = dataService.PlayerAnalytics();
        return CommonResponse.ok(response);
    }

    @Operation(summary = "추천 라켓 조회", description = "추천 라켓을 조회하는 API입니다")
    @GetMapping("/rackets/{balance}/{weight}/{shaft}/{price}")
    public CommonResponse<RacketRecommendResponse> ReccommandRacket(
            @PathVariable("balance") String balance,
            @PathVariable("weight") String weight,
            @PathVariable("shaft") String shaft,
            @PathVariable("price") String price,
            @RequestParam(value = "racket_id", required = false) List<Integer> racketIds
    ) {
        RacketRecommendRequest request = new RacketRecommendRequest(balance, weight, shaft, price, racketIds);
        RacketRecommendResponse response = dataService.RecommendRacket(request.toCommand());
        return CommonResponse.ok(response);
    }
}
