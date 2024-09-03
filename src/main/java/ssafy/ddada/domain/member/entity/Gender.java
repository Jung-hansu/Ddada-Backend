package ssafy.ddada.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");

    private final String value;

    @JsonCreator
    public static Gender parse(String input) {
        return Stream.of(Gender.values())
                .filter(gender -> gender.toString().equals(input.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
