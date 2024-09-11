package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.MatchTime;

import java.time.LocalDate;

public record MatchCreateCommand(
    Long court_id,
    MatchType matchType,
    LocalDate matchDate,
    MatchTime matchTime
) { }
