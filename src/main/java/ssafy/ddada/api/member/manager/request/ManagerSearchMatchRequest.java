package ssafy.ddada.api.member.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;

import static ssafy.ddada.common.util.ParameterUtil.nullToFalse;

@Schema(description = "매니저 할당 경기 검색 결과")
public record ManagerSearchMatchRequest(
        @Schema(description = "검색어")
        String keyword,

        @Schema(description = "오늘 경기만 조회할 지 여부")
        Boolean todayOnly,

        @Schema(description = "comma-separated 경기 상태 목록")
        String statuses,

        @Schema(description = "페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public ManagerSearchMatchCommand toCommand(){
        return new ManagerSearchMatchCommand(
                keyword,
                nullToFalse(todayOnly),
                MatchStatus.toMatchStatusSet(statuses),
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }
}
