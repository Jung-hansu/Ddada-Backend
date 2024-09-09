package ssafy.ddada.api.court.request;

import io.swagger.v3.oas.annotations.media.Schema;

// TODO: 스키마 어노테이션, 커맨드 객체 생성하기
@Schema(description = "시설 검색 요청 DTO")
public record CourtSearchRequest (
        @Schema(description = "검색 키워드", example = "문화체육관")
        String keyword,
        @Schema(description = "페이지 번호")
        int page,
        @Schema(description = "페이지 크기")
        int size
) {
}
