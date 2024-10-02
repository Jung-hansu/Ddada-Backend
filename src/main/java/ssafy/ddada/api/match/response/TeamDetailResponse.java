package ssafy.ddada.api.match.response;

import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.api.member.player.response.PlayerSimpleResponse;

public record TeamDetailResponse(
        Long id,
        PlayerSimpleResponse player1,
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
