package ssafy.ddada.domain.match.command;

import org.springframework.data.domain.Pageable;
import ssafy.ddada.domain.match.entity.MatchStatus;

public record MatchSearchCommand(
        String keyword,
        MatchStatus status,
        Pageable pageable
) {
}
