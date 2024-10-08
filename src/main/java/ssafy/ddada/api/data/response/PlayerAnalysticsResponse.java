package ssafy.ddada.api.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

// PlayerAnalysticsResponse 레코드 정의
@Schema(description = "Player Analystics 응답 DTO")
public record PlayerAnalysticsResponse(
        @Schema(description = "매치 수")
        int match,

        @Schema(description = "비율 정보")
        Rate rate,

        @Schema(description = "플레이어 타입")
        String type,

        @Schema(description = "플레이어 타입 메시지")
        @JsonProperty("type_message")
        String typeMessage
) {
    public record Rate(
            @JsonProperty("score_rate")
            @Schema(description = "득점 비율") Integer scoreRate,

            @JsonProperty("lose_rate")
            @Schema(description = "실점 비율") Integer loseRate,

            @Schema(description = "기술 비율") Integer skills,

            @Schema(description = "전략 비율") Integer strategy,

            @Schema(description = "회복 비율") Integer recovery
    ) {}
}
