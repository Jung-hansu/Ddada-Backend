package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.EarnedType;
import ssafy.ddada.domain.match.entity.MissedType;

import java.util.List;

public record MatchResultCommand(
        Integer winnerTeamNumber,
        Integer team1SetScore,
        Integer team2SetScore,
        List<SetInfoCommand> sets
) {
    public record SetInfoCommand(
            Integer setNumber,
            Integer setWinnerTeamNumber,
            Integer team1Score,
            Integer team2Score,
            List<ScoreInfoCommand> scores
    ) {
        public record ScoreInfoCommand(
                Integer scoreNumber,
                Integer earnedPlayer,
                Integer missedPlayer1,
                Integer missedPlayer2,
                EarnedType earnedType,
                MissedType missedType
        ) { }
    }
}
