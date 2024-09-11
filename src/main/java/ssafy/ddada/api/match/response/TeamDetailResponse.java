package ssafy.ddada.api.match.response;

import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.api.member.player.response.PlayerSimpleResponse;

public record TeamDetailResponse(
        Long id,
        PlayerSimpleResponse player1,
        PlayerSimpleResponse player2
) {
    public static TeamDetailResponse from(Team team){
        return new TeamDetailResponse(
                team.getId(),
                PlayerSimpleResponse.from(team.getPlayer1()),
                PlayerSimpleResponse.from(team.getPlayer2())
        );
    }
}
