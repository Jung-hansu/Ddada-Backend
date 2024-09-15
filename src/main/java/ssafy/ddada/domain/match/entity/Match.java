package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.member.manager.entity.Manager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @OneToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @OneToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private Integer winnerTeamNumber;

    private Integer team1SetScore;

    private Integer team2SetScore;

    @Column(nullable = false)
    private MatchStatus status;

    private RankType rankType;

    @Column(nullable = false)
    private MatchType matchType;

    @Column(nullable = false)
    private LocalDate matchDate;

    private LocalTime matchTime;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Set> sets = new ArrayList<>();

    public Match(Court court, Team team1, Team team2, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        this.court = court;
        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.RESERVED;
        this.matchType = matchType;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
    }

    public static Match createNewMatch(Court court, Team team1, Team team2, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        return new Match(court, team1, team2, matchType, matchDate, matchTime);
    }

}
