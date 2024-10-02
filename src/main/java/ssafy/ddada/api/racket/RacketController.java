package ssafy.ddada.api.racket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.api.racket.response.RacketSearchResponse;
import ssafy.ddada.domain.racket.service.RacketService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rackets")
@Tag(name = "Racket", description = "라켓관리")
public class RacketController {

    private final RacketService racketService;

    @Operation(summary = "라켓 검색", description = "라켓명, 브랜드명 기반 키워드로 라켓을 검색하는 API입니다.")
    @GetMapping
    public CommonResponse<RacketSearchResponse> getRackets(@RequestParam String keyword){
        log.info("getRackets >>>> keyword: {}", keyword);
        RacketSearchResponse response = racketService.getRacketsByElasticKeyword(keyword);
        return CommonResponse.ok(response);
    }

}
