package ssafy.ddada.api.match.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ssafy.ddada.domain.match.command.MatchCreateCommand;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.time.LocalDate;

@Schema(description = "경기 생성 요청 DTO")
public record MatchCreateRequest(
    @Schema(description = "시설 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시설 ID는 필수입니다.")
    Long court_id,

    @Schema(description = "팀1 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    Long team1_id,

    @Schema(description = "팀2 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    Long team2_id,

    @Schema(description = "담당 매니저 ID", example = "14")
    Long manager_id,

    @Schema(description = "단식 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean isSingle,

    @Schema(description = "경기 일자", example = "2024-09-06")
    LocalDate matchDate
) {
    public MatchCreateCommand toCommand(){
        return new MatchCreateCommand(court_id, team1_id, team2_id, manager_id, isSingle, matchDate);
    }
}
