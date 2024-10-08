package ssafy.ddada.api.data.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Player Analytics 응답 DTO")
public record PlayerAnalysticsResponse(
        @Schema(description = "매치 수")
        int match,

        @Schema(description = "비율 정보")
        Rate rate,

        @Schema(description = "플레이어 타입")
        String type,

        @Schema(description = "플레이어 타입 메시지")
        String typeMessage
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Rate(
            @Schema(description = "득점 비율")
            int scoreRate,

            @Schema(description = "실점 비율")
            int loseRate,

            @Schema(description = "기술 비율")
            int skills,

            @Schema(description = "전략 비율")
            int strategy,

            @Schema(description = "회복 비율")
            int recovery
    ) {}
}
