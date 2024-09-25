package ssafy.ddada.domain.court.command;

import org.springframework.data.domain.Pageable;
import ssafy.ddada.domain.court.entity.Region;

import java.util.Set;

public record CourtSearchCommand(
        String keyword,
        Set<Region> regions,
        Pageable pageable
) { }
