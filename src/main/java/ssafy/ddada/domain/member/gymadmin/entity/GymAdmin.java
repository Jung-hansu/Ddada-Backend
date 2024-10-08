package ssafy.ddada.domain.member.gymadmin.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.common.MemberRole;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
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

    // 회원가입 메서드
    public void signupGymAdmin(String email, String password, String number) {
        this.email = email;
        this.password = password;
        this.number = number;
        this.isDeleted = false;
        this.role = MemberRole.GYM_ADMIN;  // 기본값 설정
    }

    // 저장 전에 기본 role 설정
    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.GYM_ADMIN;  // 기본값 설정
        }
    }
}
