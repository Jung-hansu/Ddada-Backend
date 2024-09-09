package ssafy.ddada.domain.member.courtadmin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.MemberInterface;
import ssafy.ddada.domain.member.common.MemberRole;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
public class CourtAdmin extends BaseMemberEntity implements MemberInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String number;

    // 회원가입 메서드
    public void signupCourtAdmin(String email, String password, String number) {
        this.email = email;
        this.password = password;
        this.number = number;
        this.isDeleted = false;
        this.role = MemberRole.COURT_ADMIN;  // 기본값 설정
    }

    // 저장 전에 기본 role 설정
    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.COURT_ADMIN;  // 기본값 설정
        }
    }
}
