package ssafy.ddada.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import ssafy.ddada.domain.member.entity.SearchedMember;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchedTeam(
        Long id,
        SearchedMember player1,
        SearchedMember player2
) {
    public static SearchedTeam from(Team team){
        return new SearchedTeam(
                team.getId(),
                SearchedMember.from(team.getPlayer1()),
                SearchedMember.from(team.getPlayer2())
        );
    }
}
