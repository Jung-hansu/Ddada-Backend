package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import ssafy.ddada.domain.match.command.MatchSearchCommand;
import ssafy.ddada.domain.match.entity.MatchStatus;

public record MatchSearchRequest(
        @Schema(description = "검색 키워드", example = "문화체육관")
        String keyword,
        @Schema(description = "경기 상태", example = "[\"CREATED\", \"RESERVED\", \"PLAYING\", \"FINISHED\", \"CANCELED\"]")
        String status,
        @Schema(description = "페이지 번호")
        int page,
        @Schema(description = "페이지 크기")
        int size
) {
    public MatchSearchCommand toCommand(){
        return new MatchSearchCommand(
                keyword,
                MatchStatus.parse(status),
                PageRequest.of(page, size)
        );
    }
}
