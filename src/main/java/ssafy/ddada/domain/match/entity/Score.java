package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "set_id", nullable = false)
    private Set set;

    private Integer earnedPlayer;

    private Integer missedPlayer1;

    private Integer missedPlayer2;

    @Column(nullable = false)
    private Integer scoreNumber;

    @Enumerated(EnumType.STRING)
    private EarnedType earnedType;

    @Enumerated(EnumType.STRING)
    private MissedType missedType;

}
