package ssafy.ddada.domain.match.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssafy.ddada.domain.member.common.Gender;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MatchType {

    NORMAL_FREE_SINGLE("일반 자유 단식", false, Gender.NONE, true),
    NORMAL_FREE_DOUBLE("일반 자유 복식", false, Gender.NONE, false),
    NORMAL_FEMALE_SINGLE("일반 여성 단식", false, Gender.FEMALE, true),
    NORMAL_FEMALE_DOUBLE("일반 여성 복식", false, Gender.FEMALE,false),
    NORMAL_MALE_SINGLE("일반 남성 단식", false, Gender.MALE,true),
    NORMAL_MALE_DOUBLE("일반 남성 복식", false, Gender.MALE,false),
    NORMAL_MIXED_DOUBLE("일반 혼성 복식", false, Gender.MIXED,false),

    RANK_FEMALE_SINGLE("랭크 여성 단식", true, Gender.FEMALE, true),
    RANK_FEMALE_DOUBLE("랭크 여성 복식", true, Gender.FEMALE,false),
    RANK_MALE_SINGLE("랭크 남성 단식", true, Gender.MALE,true),
    RANK_MALE_DOUBLE("랭크 남성 복식", true, Gender.MALE,false),
    RANK_MIXED_DOUBLE("랭크 혼성 복식", true, Gender.MIXED,false);

    private final String value;
    private final boolean isRank;
    private final Gender genderType;
    private final boolean isSingle;

    public static MatchType getMatchType(boolean isRank, Gender genderType, boolean isSingle) {
        return Stream.of(MatchType.values())
                .filter(type -> type.isRank == isRank && type.genderType == genderType && type.isSingle == isSingle)
                .findFirst()
                .orElse(null);
    }

}
