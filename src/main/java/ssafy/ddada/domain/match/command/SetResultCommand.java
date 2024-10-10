package ssafy.ddada.domain.match.command;

import java.util.List;

public record SetResultCommand(
        Integer setNumber,
        Integer setWinnerTeamNumber,
        Integer team1Score,
        Integer team2Score,
        List<ScoreResultCommand> scores
) {
    public Integer getTeamScoreByTeamNumber(Integer teamNumber){
        return teamNumber == 1 ? team1Score : team2Score;
    }
}