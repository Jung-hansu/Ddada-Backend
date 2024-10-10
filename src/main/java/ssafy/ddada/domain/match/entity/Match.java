package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.member.player.entity.Player;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Set> sets = new ArrayList<>();

    public List<String> getTeamGender(Team team) {
        List<String> teamGender = new ArrayList<>();

        Player player1 = team.getPlayer1();
        Player player2 = team.getPlayer2();

        String malePlayer1 = null;
        String malePlayer2 = null;
        String femalePlayer1 = null;
        String femalePlayer2 = null;

        // 남자 플레이어 찾기
        if (player1 != null && player1.getGender() == Gender.MALE) {
            malePlayer1 = player1.getGender().name();
        }
        if (player2 != null && player2.getGender() == Gender.MALE) {
            if (malePlayer1 == null) {
                malePlayer1 = player2.getGender().name();
            } else {
                malePlayer2 = player2.getGender().name();
            }
        }

        // 여자 플레이어 찾기
        if (player1 != null && player1.getGender() == Gender.FEMALE) {
            femalePlayer1 = player1.getGender().name();
        }
        if (player2 != null && player2.getGender() == Gender.FEMALE) {
            if (femalePlayer1 == null) {
                femalePlayer1 = player2.getGender().name();
            } else {
                femalePlayer2 = player2.getGender().name();
            }
        }

        // 혼성팀일 경우 남자는 0번 인덱스, 여자는 1번 인덱스에 추가
        if (malePlayer1 != null && femalePlayer1 != null) {
            teamGender.add(0, malePlayer1); // 남자를 0번 인덱스에 추가
            teamGender.add(1, femalePlayer1); // 여자를 1번 인덱스에 추가
        } else if (malePlayer1 != null && malePlayer2 != null) {
            // 남자 두 명인 경우
            teamGender.add(malePlayer1);
            teamGender.add(malePlayer2);
        } else if (femalePlayer1 != null && femalePlayer2 != null) {
            // 여자 두 명인 경우
            teamGender.add(femalePlayer1);
            teamGender.add(femalePlayer2);
        } else {
            // 예약되지 않은 플레이어가 있으면 "notReserved" 추가
            if (player1 == null) {
                teamGender.add("notReserved");
            }
            if (player2 == null) {
                teamGender.add("notReserved");
            }

            // 남자나 여자가 하나만 있을 경우 추가
            if (malePlayer1 != null) {
                teamGender.add(malePlayer1);
            }
            if (femalePlayer1 != null) {
                teamGender.add(femalePlayer1);
            }
        }

        return teamGender;
    }
}