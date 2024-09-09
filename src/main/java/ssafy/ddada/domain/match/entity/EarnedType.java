package ssafy.ddada.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum EarnedType {

    CLEAR("클리어"),
    DROP("드롭"),
    HAIRPIN("헤어핀"),
    PUSH("푸시"),
    SERVE("서브"),
    SMASH("스매시");

    private final String value;

    @JsonCreator
    public static EarnedType parse(String input) {
        return Stream.of(EarnedType.values())
                .filter(earnedType -> earnedType.name().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }

}
