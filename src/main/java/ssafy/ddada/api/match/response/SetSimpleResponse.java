package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Set;

import java.util.Comparator;
import java.util.List;

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
        Integer team2Score,
        @Schema(description = "득점 목록")
        List<ScoreDetailResponse> scores
) {
    public static SetSimpleResponse from(Set set){
        return new SetSimpleResponse(
                set.getId(),
                set.getSetNumber(),
                set.getSetWinnerTeamNumber(),
                set.getTeam1Score(),
                set.getTeam2Score(),
                set.getScores()
                        .stream()
                        .map(ScoreDetailResponse::from)
                        .sorted(Comparator.comparing(ScoreDetailResponse::scoreNumber))
                        .toList()
        );
    }
}
