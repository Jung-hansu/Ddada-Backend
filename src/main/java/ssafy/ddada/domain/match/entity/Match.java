package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.court.entity.Court;

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
    @Column(nullable = false)
    private Team team1;

    @OneToOne
    @JoinColumn(name = "team_id")
    @Column(nullable = false)
    private Team team2;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team winner;

//    TODO: 매니저 추가하기
//    @ManyToOne
//    @JoinColumn(name = "manager_id")
//    private Manager manager;

    @Column(nullable = false)
    private MatchStatus status;

    @Column(nullable = false)
    private Boolean isSingle;

    @Column(nullable = false)
    private LocalDate matchDate;

    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Set> sets = new ArrayList<>();

    public Match(Court court, Team team1, Team team2, Boolean isSingle, LocalDate matchDate) {
        this(court, team1, team2, null, MatchStatus.RESERVED, isSingle, matchDate);
    }

    public Match(Court court, Team team1, Team team2, Team winner, MatchStatus status, Boolean isSingle, LocalDate matchDate) {
        this.court = court;
        this.team1 = team1;
        this.team2 = team2;
        this.winner = winner;
        this.status = status;
        this.isSingle = isSingle;
        this.matchDate = matchDate;
    }


}
