package ssafy.ddada.domain.match.command;

public record TeamChangePlayerCommand(
        Long playerId,
        Integer teamNumber,
        Integer playerNumber
) {
}
