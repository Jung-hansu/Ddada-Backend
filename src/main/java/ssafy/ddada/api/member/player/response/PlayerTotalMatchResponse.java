package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerTotalMatchResponse(
        @Schema(description = "플레이어 경기 수")
        Integer totalMatch
) {
    public static PlayerTotalMatchResponse of(Integer totalMatch) {
        return new PlayerTotalMatchResponse(totalMatch);
    }
}
