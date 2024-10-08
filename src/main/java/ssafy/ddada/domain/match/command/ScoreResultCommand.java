package ssafy.ddada.domain.match.command;

import ssafy.ddada.domain.match.entity.EarnedType;
import ssafy.ddada.domain.match.entity.MissedType;

public record ScoreResultCommand(
        Integer scoreNumber,
        Integer earnedPlayerNumber,
        Integer missedPlayer1Number,
        Integer missedPlayer2Number,
        EarnedType earnedType,
        MissedType missedType
) { }