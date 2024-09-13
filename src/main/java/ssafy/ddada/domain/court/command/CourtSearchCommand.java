package ssafy.ddada.domain.court.command;

import org.springframework.data.domain.Pageable;

public record CourtSearchCommand(
        String keyword,
        Pageable pageable,
        Long facilities
) { }
