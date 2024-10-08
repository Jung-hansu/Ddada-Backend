package ssafy.ddada.api.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "선수 경기 분석 응답 DTO")
public record PlayerMatchAnalyticsResponse(
        @Schema(description = "세트 정보")
        List<SetInfoResponse> setInfo,

        @Schema(description = "플로우 그래프")
        List<Integer> flow,

        @Schema(description = "득점/실점 비율")
        ScoreLoseRateResponse scoreLoseRate,

        @Schema(description = "기술 정보")
        SkillResponse skill,

        @Schema(description = "전략 정보")
        List<StrategyResponse> strategy
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SetInfoResponse(
            @Schema(description = "세트 번호")
            Integer setNumber,

            @Schema(description = "득점한 플레이어")
            List<Integer> earnedPlayer,

            @Schema(description = "실점한 플레이어 1")
            List<Integer> missedPlayer1,

            @Schema(description = "실점한 플레이어 2")
            List<Integer> missedPlayer2,

            @Schema(description = "득점 유형")
            List<String> earnedType,

            @Schema(description = "팀 1 점수")
            List<Integer> score1,

            @Schema(description = "팀 2 점수")
            List<Integer> score2
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ScoreLoseRateResponse(
            @Schema(description = "평균 득점율")
            double meanScoreRate,

            @Schema(description = "내 득점율")
            double myScoreRate,

            @Schema(description = "평균 실점율")
            double meanLoseRate,

            @Schema(description = "내 실점율")
            double myLoseRate
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SkillResponse(
            @Schema(description = "득점 기술 정보")
            ScoreSkillDetail score,

            @Schema(description = "실점 기술 정보")
            LoseSkillDetail lose
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ScoreSkillDetail(
            @Schema(description = "중간 기술율")
            MiddleSkillRate middleSkillRate,

            @Schema(description = "기술 비율")
            SkillRate skillRate,

            @Schema(description = "기술 비율 설명")
            SkillRateText skillRateText,

            @Schema(description = "메시지")
            String message
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record LoseSkillDetail(
            @Schema(description = "중간 실점 기술율")
            MiddleSkillRate middleLoseSkillRate,

            @Schema(description = "실점 기술 비율")
            SkillRate loseSkillRate,

            @Schema(description = "실점 기술 비율 설명")
            SkillRateText loseSkillRateText,

            @Schema(description = "메시지")
            String message
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record MiddleSkillRate(
            @Schema(description = "스매시 기술율")
            double smash,

            @Schema(description = "서브 기술율")
            double serve,

            @Schema(description = "네트 기술율")
            double net,

            @Schema(description = "푸시 기술율")
            double pushs,

            @Schema(description = "드롭 기술율")
            double drops,

            @Schema(description = "클리어 기술율")
            double clears
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SkillRate(
            @Schema(description = "드롭 비율")
            double drops,

            @Schema(description = "스매시 비율")
            double smash,

            @Schema(description = "클리어 비율")
            double clears,

            @Schema(description = "푸시 비율")
            double pushs,

            @Schema(description = "네트 비율")
            double net
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SkillRateText(
            @Schema(description = "드롭 설명")
            String drops,

            @Schema(description = "스매시 설명")
            String smash,

            @Schema(description = "클리어 설명")
            String clears,

            @Schema(description = "푸시 설명")
            String pushs,

            @Schema(description = "네트 설명")
            String net
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StrategyResponse(
            @Schema(description = "패배한 플레이어")
            Integer loser,

            @Schema(description = "실점 기술 정보")
            List<List<String>> loseSkill,

            @Schema(description = "내가 한 행동")
            List<Double> I_did,

            @Schema(description = "전략 메시지")
            String message
    ) {}
}
