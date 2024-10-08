package ssafy.ddada.domain.match.command;

import java.time.LocalDate;
import java.time.LocalTime;

public record CheckPlayerBookedCommand(
        LocalDate matchDate,
        LocalTime matchTime
) {
}
