package ssafy.ddada.domain.match.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.member.entity.Member;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "set_id")
    @Column(nullable = false)
    private Set set;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member earnedMember;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member missedMember1;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member missedMember2;

    @Column(nullable = false)
    private Integer scoreNumber;

    private EarnedType earnedType;

    private MissedType missedType;

    public Score(Set set, Member earnedMember, Member missedMember1, Member missedMember2, Integer scoreNumber, EarnedType earnedType, MissedType missedType) {
        this.set = set;
        this.earnedMember = earnedMember;
        this.missedMember1 = missedMember1;
        this.missedMember2 = missedMember2;
        this.scoreNumber = scoreNumber;
        this.earnedType = earnedType;
        this.missedType = missedType;
    }

}
