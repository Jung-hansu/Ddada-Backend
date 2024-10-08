package ssafy.ddada.domain.match.command;

import org.springframework.data.domain.Pageable;
import ssafy.ddada.domain.gym.entity.Region;
import ssafy.ddada.domain.match.entity.MatchStatus;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;

import java.util.Set;

public record MatchSearchCommand(
        String keyword,
        RankType rankType,
        Set<MatchType> matchTypes,
        Set<MatchStatus> statuses,
        Set<Region> region,
        Pageable pageable
) {
}
