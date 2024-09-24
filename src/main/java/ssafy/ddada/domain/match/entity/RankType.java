package ssafy.ddada.domain.match.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.common.exception.match.InvalidRankTypeException;

import java.util.stream.Stream;

import static ssafy.ddada.common.util.ParameterUtil.isEmptyString;

@Getter
@RequiredArgsConstructor
public enum RankType {

    NORMAL("친선"),
    RANK("랭크");

    private final String value;

    public static RankType of(String value) {
        return Stream.of(values())
                .filter(rankType -> rankType.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidRankTypeException::new);
    }

    public static RankType toRankType(String rankType) {
        if (isEmptyString(rankType)){
            return null;
        }
        return of(rankType);
    }
}
