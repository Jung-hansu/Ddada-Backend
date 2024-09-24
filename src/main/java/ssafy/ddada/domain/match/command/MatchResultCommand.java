package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.EarnedType;
import ssafy.ddada.domain.match.entity.MissedType;

import java.util.List;

public record MatchResultCommand(
        Integer winnerTeamNumber,
        Integer team1SetScore,
        Integer team2SetScore,
        List<SetResultCommand> sets
) {
    public record SetResultCommand(
            Integer setNumber,
            Integer setWinnerTeamNumber,
            Integer team1Score,
            Integer team2Score,
            List<ScoreResultCommand> scores
    ) {
        public record ScoreResultCommand(
                Integer scoreNumber,
                Integer earnedPlayer,
                Integer missedPlayer1,
                Integer missedPlayer2,
                EarnedType earnedType,
                MissedType missedType
        ) { }
    }
}
