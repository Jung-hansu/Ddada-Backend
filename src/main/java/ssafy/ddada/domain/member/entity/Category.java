package ssafy.ddada.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Category {
    SOCCER("축구"),
    BASEBALL("야구"),
    ONLINEGAME("온라인게임"),
    CONCERT("콘서트");

    private final String value;

    @JsonCreator
    public static Category parse(String input) {
        return Stream.of(Category.values())
                .filter(category -> category.name().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }
}
