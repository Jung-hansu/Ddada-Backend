package ssafy.ddada.api.match.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.api.member.player.response.PlayerSimpleResponse;

@Schema(description = "팀 세부 정보 응답 DTO")
public record TeamDetailResponse(
        @Schema(description = "팀 ID")
        Long id,

        @Schema(description = "선수 1")
        PlayerSimpleResponse player1,

        @Schema(description = "선수 2")
        PlayerSimpleResponse player2
) {
    public static TeamDetailResponse from(Team team, String player1Image, String player2Image) {
        return new TeamDetailResponse(
                team.getId(),
                PlayerSimpleResponse.from(team.getPlayer1(), player1Image),
                PlayerSimpleResponse.from(team.getPlayer2(), player2Image)
        );
    }
}
