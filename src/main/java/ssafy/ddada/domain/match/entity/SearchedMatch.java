package ssafy.ddada.domain.match.entity;

import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.SearchedCourt;
import ssafy.ddada.domain.member.entity.Manager;
import ssafy.ddada.domain.member.entity.SearchedManager;

import java.time.LocalDate;
import java.util.List;

public record SearchedMatch (
        Long id,
        SearchedCourt court,
        SearchedTeam team1,
        SearchedTeam team2,
        SearchedTeam winner,
        SearchedManager manager,
        Integer team1SetScore,
        Integer team2SetScore,
        MatchStatus status,
        Boolean isSingle,
        LocalDate matchDate,
        List<SearchedSet> sets
) {

    public static SearchedMatch from(Match match){
        return new SearchedMatch(
                match.getId(),
                SearchedCourt.from(match.getCourt()),
                SearchedTeam.from(match.getTeam1()),
                SearchedTeam.from(match.getTeam2()),
                SearchedTeam.from(match.getWinner()),
                SearchedManager.from(match.getManager()),
                match.getTeam1SetScore(),
                match.getTeam2SetScore(),
                match.getStatus(),
                match.getIsSingle(),
                match.getMatchDate(),
                match.getSets()
                        .stream()
                        .map(SearchedSet::from)
                        .toList()
        );
    }

}
