package ssafy.ddada.domain.match.command;

public record TeamCreateCommand(
        Long player1_id,
        Long player2_id
) {
}
