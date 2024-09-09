package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.MatchType;

import java.time.LocalDateTime;

public record MatchCreateCommand(
    Long court_id,
    MatchType matchType,
    LocalDateTime matchDateTime
) { }
