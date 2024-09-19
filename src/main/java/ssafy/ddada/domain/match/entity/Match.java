package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
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

    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Setter
    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private Integer winnerTeamNumber;

    private Integer team1SetScore;

    private Integer team2SetScore;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankType rankType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @Column(nullable = false)
    private LocalDate matchDate;

    private LocalTime matchTime;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Set> sets = new ArrayList<>();

    public Match(Court court, Team team1, Team team2, RankType rankType, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        this.court = court;
        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.RESERVED;
        this.rankType = rankType;
        this.matchType = matchType;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
    }

    public static Match createNewMatch(Court court, Team team1, Team team2, RankType rankType, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        return new Match(court, team1, team2, rankType, matchType, matchDate, matchTime);
    }

}
