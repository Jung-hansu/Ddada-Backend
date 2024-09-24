package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.MatchStatus;

public record MatchStatusChangeCommand(
        Long matchId,
        MatchStatus status
) { }
