package ssafy.ddada.api.court;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.court.request.CourtSearchRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.court.service.CourtService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courts")
@Tag(name = "Court", description = "코트 관리")
public class CourtController {

    private final CourtService courtService;

    @Operation(summary = "코트 검색 결과 조회", description = "페이징된 코트 검색 결과를 조회하는 API입니다.")
    @GetMapping("/search")
    public CommonResponse<?> getCourtsByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String regions,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("[CourtController] 코트 검색 결과 조회 >>>> 검색어: {}, 검색 지역: {}, 페이지 번호: {}, 페이지 크기: {}", keyword, regions, page, size);
        CourtSearchRequest request = new CourtSearchRequest(keyword, regions, page, size);
        Page<CourtSimpleResponse> response = courtService.getElasticFilteredCourts(request.toCommand());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "코트 세부 조회", description = "코트 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{court_id}")
    public CommonResponse<CourtDetailResponse> getCourtById(@PathVariable("court_id") Long courtId){
        log.info("[CourtController] 코트 세부 조회 >>>> 코트 ID: {}", courtId);

        CourtDetailResponse response = courtService.getCourtById(courtId);
        return CommonResponse.ok(response);
    }

}
