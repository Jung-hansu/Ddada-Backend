package ssafy.ddada.api.racket.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RacketSearchResponse(
        @Schema(description = "검색 결과 수")
        Integer count,
        @Schema(description = "검색 결과")
        List<RacketSimpleResponse> rackets
) {
    public static RacketSearchResponse of(Integer count, List<RacketSimpleResponse> rackets) {
        return new RacketSearchResponse(count, rackets);
    }
}
