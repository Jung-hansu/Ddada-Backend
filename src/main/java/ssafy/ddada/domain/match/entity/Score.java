package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private Set set;

    private Integer earnedMember;

    private Integer missedMember1;

    private Integer missedMember2;

    @Column(nullable = false)
    private Integer scoreNumber;

    @Enumerated(EnumType.STRING)
    private EarnedType earnedType;

    @Enumerated(EnumType.STRING)
    private MissedType missedType;

    public Score(Set set, Integer earnedMember, Integer missedMember1, Integer missedMember2, Integer scoreNumber, EarnedType earnedType, MissedType missedType) {
        this.set = set;
        this.earnedMember = earnedMember;
        this.missedMember1 = missedMember1;
        this.missedMember2 = missedMember2;
        this.scoreNumber = scoreNumber;
        this.earnedType = earnedType;
        this.missedType = missedType;
    }

}
