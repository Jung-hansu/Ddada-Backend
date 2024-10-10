package ssafy.ddada.domain.member.gymadmin.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.common.MemberRole;

@Getter
@Entity
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GymAdmin extends BaseMemberEntity implements Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_admin_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Gym gym;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String number;

    @Setter
    private Integer cumulativeIncome;

    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.GYM_ADMIN;  // 기본값 설정
        }
    }

    @Builder
    public GymAdmin(Gym gym, String email, String password, String nickname, String number, Integer cumulativeIncome) {
        this.gym = gym;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.number = number;
        this.cumulativeIncome = cumulativeIncome;
        this.role = MemberRole.GYM_ADMIN;
        this.isDeleted = false;
    }
}
