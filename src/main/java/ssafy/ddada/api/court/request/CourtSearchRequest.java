package ssafy.ddada.api.court.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Facility;

import java.util.Set;

// TODO: 스키마 어노테이션, 커맨드 객체 생성하기
@Schema(description = "시설 검색 요청 DTO")
public record CourtSearchRequest (
        @Schema(description = "검색 키워드", example = "문화체육관")
        String keyword,
        @Schema(description = "페이지 번호")
        int page,
        @Schema(description = "페이지 크기")
        int size,
        @Schema(description = "필터링할 편의시설 목록", example = "[\"PARKING\", \"SHOWER\", \"TOILET\", \"WIFI\"]")
        Set<Facility> facilities
) {
        public CourtSearchCommand toCommand() {
                return new CourtSearchCommand(
                        keyword,
                        page,
                        size,
                        Facility.bitMask(facilities)
                );
        }
}
