package ssafy.ddada.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.common.exception.Exception.Match.InvalidMatchStatusException;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MatchStatus {

    CREATED("모집중"),
    RESERVED("예약됨"),
    PLAYING("진행중"),
    FINISHED("종료됨"),
    CANCELED("취소됨");

    private final String value;

    @JsonCreator
    public static MatchStatus parse(String input) {
        return Stream.of(MatchStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(InvalidMatchStatusException::new);
    }

}
