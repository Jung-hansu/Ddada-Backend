package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreateCommand(
    Long court_id,
    RankType rankType,
    MatchType matchType,
    LocalDate matchDate,
    LocalTime matchTime
) { }
