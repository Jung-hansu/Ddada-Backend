package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    private Integer playerCount;

    private Integer rating;

    public Team(Player player1, Player player2, Integer playerCount, Integer rating) {
        this.player1 = player1;
        this.player2 = player2;
        this.playerCount = playerCount;
        this.rating = rating;
    }

    public static Team createNewTeam(Player creator){
        return new Team(creator, null, 1, creator.getRating());
    }

    public static Team createNewTeam(){
        return new Team(null, null, 0, 0);
    }

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
