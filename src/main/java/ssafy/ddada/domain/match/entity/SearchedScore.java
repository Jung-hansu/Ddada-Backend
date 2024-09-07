package ssafy.ddada.domain.match.entity;

import ssafy.ddada.domain.member.entity.SearchedMember;

public record SearchedScore(
        Integer scoreNumber,
        SearchedMember earnedMember,
        SearchedMember missedMember1,
        SearchedMember missedMember2,
        EarnedType earnedType,
        MissedType missedType
) {
    public static SearchedScore from(Score score){
        return new SearchedScore(
                score.getScoreNumber(),
                SearchedMember.from(score.getEarnedMember()),
                SearchedMember.from(score.getMissedMember1()),
                SearchedMember.from(score.getMissedMember2()),
                score.getEarnedType(),
                score.getMissedType()
        );
    }
}
