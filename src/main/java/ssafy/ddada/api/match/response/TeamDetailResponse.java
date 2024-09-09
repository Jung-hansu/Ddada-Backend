package ssafy.ddada.api.match.response;

import ssafy.ddada.domain.match.entity.Team;
import ssafy.ddada.api.member.response.MemberSimpleResponse;

public record TeamDetailResponse(
        Long id,
        MemberSimpleResponse player1,
        MemberSimpleResponse player2
) {
    public static TeamDetailResponse from(Team team){
        return new TeamDetailResponse(
                team.getId(),
                MemberSimpleResponse.from(team.getPlayer1()),
                MemberSimpleResponse.from(team.getPlayer2())
        );
    }
}
