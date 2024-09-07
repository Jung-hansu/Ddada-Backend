package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.member.entity.Manager;

import java.time.LocalDate;
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
    @JoinColumn(name = "court_id")
    @Column(nullable = false)
    private Court court;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team1;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team2;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team winner;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private Integer team1SetScore;

    private Integer team2SetScore;

    @Column(nullable = false)
    private MatchStatus status;

    @Column(nullable = false)
    private Boolean isSingle;

    @Column(nullable = false)
    private LocalDate matchDate;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Set> sets = new ArrayList<>();

    public Match(Court court, Team team1, Team team2, Manager manager, Boolean isSingle, LocalDate matchDate) {
        this(court, team1, team2, null, manager, 0, 0, MatchStatus.RESERVED, isSingle, matchDate);
    }

    public Match(Court court, Team team1, Team team2, Team winner, Manager manager, Integer team1SetScore, Integer team2SetScore, MatchStatus status, Boolean isSingle, LocalDate matchDate) {
        this.court = court;
        this.team1 = team1;
        this.team2 = team2;
        this.winner = winner;
        this.manager = manager;
        this.team1SetScore = team1SetScore;
        this.team2SetScore = team2SetScore;
        this.status = status;
        this.isSingle = isSingle;
        this.matchDate = matchDate;
    }

    public static Match createMatch(Court court, Team team1, Team team2, Manager manager, Boolean isSingle, LocalDate matchDate) {
        return new Match(court, team1, team2, manager, isSingle, matchDate);
    }

}
