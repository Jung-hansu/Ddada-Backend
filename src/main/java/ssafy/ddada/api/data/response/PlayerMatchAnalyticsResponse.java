package ssafy.ddada.api.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Player Match Analytics 응답 DTO")
public record PlayerMatchAnalyticsResponse(
        @JsonProperty("set_info")
        @Schema(description = "세트 정보")
        List<SetInfoResponse> setInfo,

        @Schema(description = "플로우 그래프")
        List<Integer> flow,

        @JsonProperty("score_lose_rate")
        @Schema(description = "득점/실점 비율")
        ScoreLoseRateResponse scoreLoseRate,

        @Schema(description = "기술 정보")
        SkillResponse skill,

        @Schema(description = "전략 정보")
        List<StrategyResponse> strategy
) {
    public record SetInfoResponse(
            @JsonProperty("set_number")
            @Schema(description = "세트 번호") Integer setNumber,

            @JsonProperty("earned_player")
            @Schema(description = "득점한 플레이어") List<Integer> earnedPlayer,

            @JsonProperty("missed_player1")
            @Schema(description = "실점한 플레이어 1") List<Integer> missedPlayer1,

            @JsonProperty("missed_player2")
            @Schema(description = "실점한 플레이어 2") List<Integer> missedPlayer2,

            @JsonProperty("earned_type")
            @Schema(description = "득점 유형") List<String> earnedType,

            @JsonProperty("score1")
            @Schema(description = "팀 1 점수") List<Integer> score1,

            @JsonProperty("score2")
            @Schema(description = "팀 2 점수") List<Integer> score2
    ) {}

    public record ScoreLoseRateResponse(
            @JsonProperty("mean_score_rate")
            @Schema(description = "평균 득점율") double meanScoreRate,

            @JsonProperty("my_score_rate")
            @Schema(description = "내 득점율") double myScoreRate,

            @JsonProperty("mean_lose_rate")
            @Schema(description = "평균 실점율") double meanLoseRate,

            @JsonProperty("my_lose_rate")
            @Schema(description = "내 실점율") double myLoseRate
    ) {}

    public record SkillResponse(
            @Schema(description = "득점 기술 정보") ScoreSkillDetail score,
            @Schema(description = "실점 기술 정보") LoseSkillDetail lose
    ) {}

    public record ScoreSkillDetail(
            @JsonProperty("middle_skill_rate")
            @Schema(description = "중간 기술율") MiddleSkillRate middleSkillRate,

            @JsonProperty("skill_rate")
            @Schema(description = "기술 비율") SkillRate skillRate,

            @JsonProperty("skill_rate_text")
            @Schema(description = "기술 비율 설명") SkillRateText skillRateText,

            @Schema(description = "메시지") String message
    ) {}

    public record LoseSkillDetail(
            @JsonProperty("middle_lose_skill_rate")
            @Schema(description = "중간 실점 기술율") MiddleSkillRate middleLoseSkillRate,

            @JsonProperty("lose_skill_rate")
            @Schema(description = "실점 기술 비율") SkillRate loseSkillRate,

            @JsonProperty("lose_skill_rate_text")
            @Schema(description = "실점 기술 비율 설명") SkillRateText loseSkillRateText,

            @Schema(description = "메시지") String message
    ) {}

    public record MiddleSkillRate(
            @JsonProperty("smash")
            @Schema(description = "스매시 기술율") double smash,

            @JsonProperty("serve")
            @Schema(description = "서브 기술율") double serve,

            @JsonProperty("net")
            @Schema(description = "네트 기술율") double net,

            @JsonProperty("pushs")
            @Schema(description = "푸시 기술율") double pushs,

            @JsonProperty("drops")
            @Schema(description = "드롭 기술율") double drops,

            @JsonProperty("clears")
            @Schema(description = "클리어 기술율") double clears
    ) {}

    public record SkillRate(
            @JsonProperty("drops")
            @Schema(description = "드롭 비율") double drops,

            @JsonProperty("smash")
            @Schema(description = "스매시 비율") double smash,

            @JsonProperty("clears")
            @Schema(description = "클리어 비율") double clears,

            @JsonProperty("pushs")
            @Schema(description = "푸시 비율") double pushs,

            @JsonProperty("net")
            @Schema(description = "네트 비율") double net
    ) {}

    public record SkillRateText(
            @JsonProperty("drops")
            @Schema(description = "드롭 설명") String drops,

            @JsonProperty("smash")
            @Schema(description = "스매시 설명") String smash,

            @JsonProperty("clears")
            @Schema(description = "클리어 설명") String clears,

            @JsonProperty("pushs")
            @Schema(description = "푸시 설명") String pushs,

            @JsonProperty("net")
            @Schema(description = "네트 설명") String net
    ) {}

    public record StrategyResponse(
            @Schema(description = "패배한 플레이어") Integer loser,

            @JsonProperty("lose_skill")
            @Schema(description = "실점 기술 정보") List<List<String>> loseSkill,

            @JsonProperty("I_did")
            @Schema(description = "내가 한 행동") List<Double> I_did,

            @Schema(description = "전략 메시지") String message
    ) {}
}
