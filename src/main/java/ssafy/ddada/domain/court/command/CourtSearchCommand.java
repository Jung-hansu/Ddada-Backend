package ssafy.ddada.domain.court.command;

import ssafy.ddada.domain.court.entity.Facility;

import java.util.Set;

public record CourtSearchCommand(
        String keyword,
        int page,
        int size,
        Set<Facility> facilities
) { }
