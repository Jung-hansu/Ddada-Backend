package ssafy.ddada.domain.match.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchDocument {

    private LocalDate matchDate;

    private LocalTime matchTime;

    public static MatchDocument from(Match match) {
        return MatchDocument.builder()
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .build();
    }

}
