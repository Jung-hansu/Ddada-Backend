package ssafy.ddada.api.court.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.entity.Court;

@Schema(description = "시설 검색 결과 DTO")
public record CourtDetailResponse(
        @Schema(description = "시설 ID")
        Long id,
        @Schema(description = "시설명")
        String name,
        @Schema(description = "시설 주소")
        String address,
        @Schema(description = "시설 전화번호")
        String contactNumber,
        @Schema(description = "시설 설명")
        String description
) {
    public static CourtDetailResponse from(Court court){
        return new CourtDetailResponse(
                court.getId(),
                court.getName(),
                court.getAddress(),
                court.getContactNumber(),
                court.getDescription()
        );
    }
}
