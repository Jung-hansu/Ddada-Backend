package ssafy.ddada.domain.data.command;

import java.util.List;

public record RacketRecommendCommand(
        String balance,
        String weight,
        String shaft,
        String price,
        List<Integer> racketIds
) {
}
