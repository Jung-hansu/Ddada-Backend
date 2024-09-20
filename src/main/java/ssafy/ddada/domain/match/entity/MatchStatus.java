package ssafy.ddada.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.common.exception.match.InvalidMatchStatusException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static ssafy.ddada.common.util.ParameterUtil.isEmptyString;

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
    public static MatchStatus of(String input) {
        if (isEmptyString(input)){
            return null;
        }
        return Stream.of(MatchStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(InvalidMatchStatusException::new);
    }

    public static Set<MatchStatus> toMatchStatusSet(String statuses){
        if (isEmptyString(statuses)){
            return null;
        }
        return Stream.of(statuses.split(","))
                .map(MatchStatus::of)
                .collect(Collectors.toSet());
    }

}
