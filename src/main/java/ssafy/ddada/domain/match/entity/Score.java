package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score extends BaseMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private Set set;

    private String earnedMember;

    private String missedMember1;

    private String missedMember2;

    @Column(nullable = false)
    private Integer scoreNumber;

    @Enumerated(EnumType.STRING)
    private EarnedType earnedType;

    @Enumerated(EnumType.STRING)
    private MissedType missedType;

    public Score(Set set, String earnedMember, String missedMember1, String missedMember2, Integer scoreNumber, EarnedType earnedType, MissedType missedType) {
        this.set = set;
        this.earnedMember = earnedMember;
        this.missedMember1 = missedMember1;
        this.missedMember2 = missedMember2;
        this.scoreNumber = scoreNumber;
        this.earnedType = earnedType;
        this.missedType = missedType;
    }

}
