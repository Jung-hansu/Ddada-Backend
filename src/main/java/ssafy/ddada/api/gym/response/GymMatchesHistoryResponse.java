package ssafy.ddada.api.gym.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Map;

public record GymMatchesHistoryResponse(
        @Schema(description = "일주일간 경기 수 현황")
        Map<LocalDate, Integer> history
) {
        public static GymMatchesHistoryResponse of(Map<LocalDate, Integer> history) {
                return new GymMatchesHistoryResponse(history);
        }
}
