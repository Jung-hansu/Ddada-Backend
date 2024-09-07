package ssafy.ddada.api.match.response;

import ssafy.ddada.domain.member.entity.Member;

public record TeamDetailResponse(
        Long id,
        Player player1,
        Player player2
) {
    public record Player(
            Long id,
            String nickname
    ) { }

    public static TeamDetailResponse of(Long teamId, Member player1, Member player2) {
        return new TeamDetailResponse(
                teamId,
                new Player(player1.getId(), player1.getNickname()),
                new Player(player2.getId(), player2.getNickname())
        );
    }
}
