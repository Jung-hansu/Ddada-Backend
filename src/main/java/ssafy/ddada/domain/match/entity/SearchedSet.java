package ssafy.ddada.domain.match.entity;

import java.util.List;

public record SearchedSet(
        Integer setNumber,
        SearchedTeam winner,
        List<SearchedScore> scores
) {
    public static SearchedSet from(Set set){
        return new SearchedSet(
                set.getSetNumber(),
                SearchedTeam.from(set.getWinner()),
                set.getScores()
                        .stream()
                        .map(SearchedScore::from)
                        .toList()
        );
    }
}
