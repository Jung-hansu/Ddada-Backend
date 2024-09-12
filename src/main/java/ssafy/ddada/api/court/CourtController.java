package ssafy.ddada.api.court;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.request.CourtSearchRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.service.CourtService;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courts")
@Tag(name = "Court", description = "시설관리")
public class CourtController {

    private final CourtService courtService;

    @Operation(summary = "시설 검색 결과 조회", description = "페이징된 시설 검색 결과를 조회하는 API입니다.")
    @GetMapping("/search")
    public CommonResponse<Page<CourtSimpleResponse>> getCourtsByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String facilities) {


        CourtSearchRequest request = new CourtSearchRequest(keyword, page, size, facilities);
        log.info("시설 검색 결과 조회 >>>> 검색어: {}, 페이지 번호: {}, 페이지 크기: {}, 편의시설: {}", request.keyword(), request.page(), request.size(), request.facilities());

        Page<CourtSimpleResponse> response = courtService.getCourtsByKeyword(request.toCommand());
        return CommonResponse.ok(response);
    }

    @Operation(summary = "시설 세부 조회", description = "시설 세부 정보를 조회하는 api입니다.")
    @GetMapping("/{court_id}")
    public CommonResponse<CourtDetailResponse> getCourtById(@PathVariable("court_id") Long courtId){
        log.info("시설 세부 조회 >>>> 시설 ID: {}", courtId);

        CourtDetailResponse response = courtService.getCourtById(courtId);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "배드민턴 코트 추가", description = "새로운 배드민턴 코트를 추가하는 API입니다.")
    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> createBadmintonCourt(
            @ModelAttribute @Validated CourtCreateRequest request) {
        courtService.createBadmintonCourt(request);
        return ResponseEntity.ok().build();
    }

}
