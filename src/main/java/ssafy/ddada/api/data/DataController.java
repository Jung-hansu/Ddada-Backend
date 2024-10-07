package ssafy.ddada.api.data;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.data.response.PlayerAnalysticsResponse;
import ssafy.ddada.api.data.response.PlayerMatchAnalyticsResponse;
import ssafy.ddada.domain.data.service.DataService;

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
    @GetMapping("/player/")
    public CommonResponse<PlayerAnalysticsResponse> PlayerAnalytics() {
        PlayerAnalysticsResponse response = dataService.PlayerAnalytics();
        return CommonResponse.ok(response);
    }
}
