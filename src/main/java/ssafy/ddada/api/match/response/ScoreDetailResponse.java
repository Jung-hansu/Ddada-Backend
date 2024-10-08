package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.EarnedType;
import ssafy.ddada.domain.match.entity.MissedType;
import ssafy.ddada.domain.match.entity.Score;

@Schema(description = "득점 세부 정보 응답 DTO")
public record ScoreDetailResponse(
        @Schema(description = "득점 번호(정렬 기준)")
        Integer scoreNumber,

        @Schema(description = "득점 선수 번호")
        Integer earnedMember,

        @Schema(description = "실점 선수 번호1")
        Integer missedMember1,

        @Schema(description = "실점 선수 번호2")
        Integer missedMember2,

        @Schema(description = "득점 방식")
        EarnedType earnedType,

        @Schema(description = "실점 방식")
        MissedType missedType
) {
    public static ScoreDetailResponse from(Score score){
        return new ScoreDetailResponse(
                score.getScoreNumber(),
                score.getEarnedPlayer(),
                score.getMissedPlayer1(),
                score.getMissedPlayer2(),
                score.getEarnedType(),
                score.getMissedType()
        );
    }
}
