package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id")
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id")
    private Player player2;

    private int playerCount;

    private int rating;

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        if (player1 != null) {
            players.add(player1);
        }
        if (player2 != null) {
            players.add(player2);
        }
        return players;
    }
}
