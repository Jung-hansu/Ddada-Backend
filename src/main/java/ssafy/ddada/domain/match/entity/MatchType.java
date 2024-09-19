package ssafy.ddada.domain.match.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.common.exception.exception.match.InvalidMatchTypeException;
import ssafy.ddada.domain.member.common.Gender;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static ssafy.ddada.common.util.ParameterUtil.*;

@Getter
@RequiredArgsConstructor
public enum MatchType {

    FEMALE_SINGLE("여성단식", Gender.FEMALE, true),
    MALE_SINGLE("남성단식", Gender.MALE,true),
    FEMALE_DOUBLE("여성복식", Gender.FEMALE,false),
    MALE_DOUBLE("남성복식", Gender.MALE,false),
    MIXED_DOUBLE("혼합복식", Gender.MIXED,false);

    private final String value;
    private final Gender genderType;
    private final boolean isSingle;

    public static MatchType of(String value){
        return Stream.of(values())
                .filter(matchType -> matchType.name().equals(value))
                .findFirst()
                .orElseThrow(InvalidMatchTypeException::new);
    }

    public static Set<MatchType> toMatchTypeSet(String matchTypes) {
        if (isEmptyString(matchTypes)) {
            return null;
        }
        return Stream.of(matchTypes.split(","))
                .map(MatchType::of)
                .collect(Collectors.toSet());
    }

}
