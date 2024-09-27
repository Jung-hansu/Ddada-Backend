package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.member.player.entity.Player;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_change_id")
    private Long id;

    private Integer ratingChange;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    public static RatingChange createRatingChange(Integer ratingChange, Player player, Match match) {
        return new RatingChange(null, ratingChange, player, match);
    }
}
