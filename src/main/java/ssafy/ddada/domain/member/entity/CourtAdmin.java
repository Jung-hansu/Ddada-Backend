package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.ddada.domain.member.entity.BaseEntity;
import ssafy.ddada.domain.member.entity.MemberInterface;
import ssafy.ddada.domain.member.entity.MemberRole;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
public class CourtAdmin extends BaseEntity implements MemberInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String number;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // 회원가입 메서드
    public void signupMember(String email, String password, String number) {
        this.email = email;
        this.password = password;
        this.number = number;
        this.isDeleted = false;
        this.role = MemberRole.COURT_ADMIN;  // 기본값 설정
    }

    // 회원 삭제 메서드
    public void deleteMember() {
        this.isDeleted = true;
    }

    // 저장 전에 기본 role 설정
    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.COURT_ADMIN;  // 기본값 설정
        }
    }
}
