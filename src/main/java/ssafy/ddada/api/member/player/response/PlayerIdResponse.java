package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선수 ID 응답 DTO")
public record PlayerIdResponse(
        @Schema(description = "회원 ID", example = "1")
        Long playerId
) {
    public static PlayerIdResponse of(Long playerId) {
        return new PlayerIdResponse(playerId);
    }
}
