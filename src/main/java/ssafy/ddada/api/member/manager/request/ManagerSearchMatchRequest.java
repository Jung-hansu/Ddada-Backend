package ssafy.ddada.api.member.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.manager.command.ManagerSearchMatchCommand;

import static ssafy.ddada.common.util.ParameterUtil.nullToFalse;

@Schema(description = "매니저 할당 경기 검색 결과")
public record ManagerSearchMatchRequest(
        Long managerId,
        String keyword,
        Boolean todayOnly,
        String statuses,
        int page,
        int size
) {
    public ManagerSearchMatchCommand toCommand(){
        return new ManagerSearchMatchCommand(
                managerId,
                keyword,
                nullToFalse(todayOnly),
                MatchStatus.toMatchStatusSet(statuses),
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }
}
