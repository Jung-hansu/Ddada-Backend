package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.command.TeamCreateCommand;

@Schema(description = "팀 생성 요청 DTO")
public record TeamCreateRequest(
        @Schema(description = "선수1 ID")
        Long player1_id,
        @Schema(description = "선수2 ID")
        Long player2_id
) {
    public TeamCreateCommand toCommand(){
        return new TeamCreateCommand(player1_id, player2_id);
    }
}
