package ssafy.ddada.api.racket.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.racket.entity.Racket;

public record RacketDetailResponse(
        @Schema(description = "라켓 ID")
        Long id,
        @Schema(description = "라켓명")
        String name,
        @Schema(description = "라켓 제조사")
        String manufacturer,
        @Schema(description = "라켓 무게")
        String weight,
        @Schema(description = "라켓 소재")
        String material,
        @Schema(description = "라켓 이미지")
        String image
) {
    public static RacketDetailResponse from(Racket racket) {
        return new RacketDetailResponse(
                racket.getId(),
                racket.getName(),
                racket.getManufacturer(),
                racket.getWeight(),
                racket.getMaterial(),
                racket.getImage()
        );
    }
}
