package ssafy.ddada.api.member.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.manager.command.ManagerMatchStatusChangeCommand;

@Schema(description = "매니저 경기 상태 변경 요청 DTO")
public record ManagerMatchStatusChangeRequest(
        @Schema(description = "변경할 경기 상태")
        MatchStatus status
) {
    public ManagerMatchStatusChangeCommand toCommand(){
        return new ManagerMatchStatusChangeCommand(status);
    }
}
