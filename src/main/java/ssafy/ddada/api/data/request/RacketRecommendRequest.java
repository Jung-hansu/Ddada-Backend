package ssafy.ddada.api.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.data.command.RacketRecommendCommand;

import java.util.List;

public record RacketRecommendRequest(
        @Schema(description = "밸런스", example = "light")
        String balance,
        @Schema(description = "무게", example = "20")
        String weight,
        @Schema(description = "샤프트", example = "stiff")
        String shaft,
        @Schema(description = "가격", example = "100000")
        String price,
        @Schema(description = "라켓 리스트", example = "[1, 2, 3]")
        List<Integer> racketIds
) {
        public RacketRecommendCommand toCommand() {
                return new RacketRecommendCommand(
                        balance,
                        weight,
                        shaft,
                        price,
                        racketIds
                );
        }
}
