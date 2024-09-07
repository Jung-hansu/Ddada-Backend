package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.member.entity.Member;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member player1;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member player2;

    public Team(Member player1, Member player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public static Team createTeam(Member player1, Member player2) {
        return new Team(player1, player2);
    }

}
