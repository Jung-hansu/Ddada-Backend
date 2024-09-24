package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.command.MatchStatusChangeCommand;
import ssafy.ddada.domain.match.entity.MatchStatus;

@Schema(description = "경기 상태 변경 요청 DTO")
public record MatchStatusChangeRequest(
        @Schema(description = "경기 ID")
        Long matchId,
        @Schema(description = "변경할 경기 상태", example = "PLAYING")
        MatchStatus status
) {
    public MatchStatusChangeCommand toCommand(){
        return new MatchStatusChangeCommand(matchId, status);
    }
}
