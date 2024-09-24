package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Set;

@Schema(description = "세트 정보 요약 응답 DTO")
public record SetSimpleResponse(
        @Schema(description = "세트 ID")
        Long id,
        @Schema(description = "세트 번호")
        Integer setNumber,
        @Schema(description = "세트 승리 팀 번호")
        Integer setWinnerTeamNumber,
        @Schema(description = "팀1 점수")
        Integer team1Score,
        @Schema(description = "팀2 점수")
        Integer team2Score
) {
    public static SetSimpleResponse from(Set set){
        return new SetSimpleResponse(
                set.getId(),
                set.getSetNumber(),
                set.getSetWinnerTeamNumber(),
                set.getTeam1Score(),
                set.getTeam2Score()
        );
    }
}
