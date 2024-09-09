package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Manager extends BaseEntity implements MemberInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String profileImg;

    @Column(nullable = true)
    private String number;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // 회원가입 메서드
    public void signupMember(String email, String password, String nickname, String profileImg, String number, String description) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.number = number;
        this.description = description;
        this.isDeleted = false;
        this.role = MemberRole.MANAGER;  // 기본값 설정
    }

    // 회원 삭제 메서드
    public void deleteMember() {
        this.isDeleted = true;
    }

    // 저장 전에 기본 role 설정
    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.MANAGER;  // 기본값 설정
        }
    }
}
