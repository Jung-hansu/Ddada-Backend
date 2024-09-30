package ssafy.ddada.domain.member.manager.command;

import ssafy.ddada.domain.match.entity.MatchStatus;

public record ManagerMatchStatusChangeCommand(
        MatchStatus status
) { }
