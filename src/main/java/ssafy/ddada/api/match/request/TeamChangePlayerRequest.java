package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.command.TeamChangePlayerCommand;

public record TeamChangePlayerRequest(
        @Schema(description = "변경할 선수 ID")
        Long playerId,
        @Schema(description = "변경을 적용할 팀 번호")
        Integer teamNumber,
        @Schema(description = "변경을 적용할 선수 번호")
        Integer playerNumber
) {
    public TeamChangePlayerCommand toCommand(){
        return new TeamChangePlayerCommand(playerId, teamNumber, playerNumber);
    }
}
