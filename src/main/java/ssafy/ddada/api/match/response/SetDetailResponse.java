package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Set;

import java.util.List;

@Schema(description = "세트 세부 정보 응답 DTO")
public record SetDetailResponse(
        @Schema(description = "세트 번호(정렬 기준)")
        Integer setNumber,
        @Schema(description = "세트 승리 팀 번호")
        Integer setWinnerTeamNumber,
        @Schema(description = "득점 세부 정보 리스트")
        List<ScoreDetailResponse> scores
) {
    public static SetDetailResponse from(Set set){
        return new SetDetailResponse(
                set.getSetNumber(),
                set.getSetWinnerTeamNumber(),
                set.getScores()
                        .stream()
                        .map(ScoreDetailResponse::from)
                        .toList()
        );
    }
}
