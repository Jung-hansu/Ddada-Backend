package ssafy.ddada.api.member.manager.request;

import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.member.manager.command.ManagerMatchStatusChangeCommand;

public record ManagerMatchStatusChangeRequest(
        MatchStatus status
) {
    public ManagerMatchStatusChangeCommand toCommand(){
        return new ManagerMatchStatusChangeCommand(status);
    }
}
