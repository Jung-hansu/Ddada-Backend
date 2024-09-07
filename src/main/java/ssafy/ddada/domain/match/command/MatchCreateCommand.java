package ssafy.ddada.domain.match.command;

import java.time.LocalDate;

public record MatchCreateCommand(
    Long court_id,
    Long team1_id,
    Long team2_id,
    Long manager_id,
    Boolean isSingle,
    LocalDate matchDate
) { }
