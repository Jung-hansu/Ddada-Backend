package ssafy.ddada.domain.member.manager.command;

import org.springframework.data.domain.Pageable;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.util.Set;

public record ManagerSearchMatchCommand(
        String keyword,
        boolean todayOnly,
        Set<MatchStatus> statuses,
        Pageable pageable
) {
}
