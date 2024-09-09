package ssafy.ddada.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MissedType {

    SERVE_MISS("서브 미스");

    private final String value;

    @JsonCreator
    public static MissedType parse(String input) {
        return Stream.of(MissedType.values())
                .filter(missedType -> missedType.name().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }

}
