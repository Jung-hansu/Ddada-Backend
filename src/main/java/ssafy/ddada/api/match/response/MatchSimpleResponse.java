package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경기 정보 요약 응답 DTO")
public record MatchSimpleResponse(
        @Schema(description = "경기 ID")
        Long id,
        @Schema(description = "시설명")
        String courtName,
        @Schema(description = "시설 주소")
        String courtAddress
) {
}
