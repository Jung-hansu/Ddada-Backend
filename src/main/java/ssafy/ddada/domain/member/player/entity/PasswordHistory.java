package ssafy.ddada.domain.member.player.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    public PasswordHistory(Player player, String password) {
        this.player = player;
        this.password = password;
        this.changedAt = LocalDateTime.now();
    }
}
