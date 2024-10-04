package ssafy.ddada.domain.court.command;

import org.springframework.data.domain.Pageable;

import java.util.Set;

public record CourtSearchCommand(
        String keyword,
        Set<String> regions,
        Pageable pageable
) { }
