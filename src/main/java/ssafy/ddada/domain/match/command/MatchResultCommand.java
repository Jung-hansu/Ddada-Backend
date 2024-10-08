package ssafy.ddada.domain.match.command;

import java.util.List;

public record MatchResultCommand(
        Integer winnerTeamNumber,
        Integer team1SetScore,
        Integer team2SetScore,
        List<SetResultCommand> sets
) { }
