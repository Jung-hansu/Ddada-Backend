package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
public class CourtAdmin extends BaseEntity implements MemberInterface{
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

    // 회원가입 메서드
    public void signupMember(String email, String password, String number) {
        this.email = email;
        this.password = password;
        this.number = number;
        this.isDeleted = false;
    }

    // 회원 삭제 메서드
    public void deleteMember() {
        this.isDeleted = true;
    }
}
