package ssafy.ddada.api.court.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;

@Schema(description = "시설 정보 요약 응답 DTO")
public record CourtSimpleResponse(
        @Schema(description = "시설 ID")
        Long id,
        @Schema(description = "시설명")
        String name,
        @Schema(description = "시설 주소")
        String address
) {
    public static CourtSimpleResponse from(Court court){
        return new CourtSimpleResponse(court.getId(), court.getName(), court.getAddress());
    }
}