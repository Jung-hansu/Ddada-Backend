package ssafy.ddada.domain.match.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchTime {

    TEN("10:00"),
    TWELVE("12:00"),
    FOURTEEN("14:00"),
    SIXTEEN("16:00"),
    EIGHTEEN("18:00"),
    TWENTY("20:00");

    private final String value;

    public static MatchTime valueOf(Integer value) {
        return switch(value) {
            case 10 -> TEN;
            case 12 -> TWELVE;
            case 14 -> FOURTEEN;
            case 16 -> SIXTEEN;
            case 18 -> EIGHTEEN;
            case 20 -> TWENTY;
            default -> null;
        };
    }
}
