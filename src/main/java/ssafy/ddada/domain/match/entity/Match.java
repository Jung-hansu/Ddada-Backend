package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.member.manager.entity.Manager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Setter
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

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Set> sets = new ArrayList<>();

    public Match(Court court, Team team1, Team team2, RankType rankType, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        this.court = court;
        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.CREATED;
        this.rankType = rankType;
        this.matchType = matchType;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
    }

    public static Match createNewMatch(Court court, Team team1, Team team2, RankType rankType, MatchType matchType, LocalDate matchDate, LocalTime matchTime) {
        return new Match(court, team1, team2, rankType, matchType, matchDate, matchTime);
    }

    public List<String> getPlayersGender() {
        List<String> playersGender = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            playersGender.add("notReserved");
        }
        if (team1.getPlayer1() != null) {
            playersGender.set(0, team1.getPlayer1().getGender().name());
        }
        if (team1.getPlayer2() != null) {
            playersGender.set(1, team1.getPlayer2().getGender().name());
        }
        if (team2.getPlayer1() != null) {
            playersGender.set(2, team2.getPlayer1().getGender().name());
        }
        if (team2.getPlayer2() != null) {
            playersGender.set(3, team2.getPlayer2().getGender().name());
        }
        return playersGender;
    }
}
