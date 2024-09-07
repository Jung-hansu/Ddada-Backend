package ssafy.ddada.api.court.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import ssafy.ddada.domain.court.entity.SearchedCourt;

@Schema(description = "시설 리스트 응답 DTO")
public record CourtSearchResponse(
        @Schema(description = "시설 리스트")
        Page<SearchedCourt> courts
) {
    public static CourtSearchResponse of(Page<SearchedCourt> courts){
        return new CourtSearchResponse(courts);
    }
}
