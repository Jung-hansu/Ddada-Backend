package ssafy.ddada.domain.member.manager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.MemberInterface;
import ssafy.ddada.domain.member.common.MemberRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Manager extends BaseMemberEntity implements MemberInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImg;

    private String number;

    @Column(nullable = false)
    private String description;

    // 매니저 회원가입 메서드
    public void signupManager(String email, String password, String nickname, String profileImg, String number, String description) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.number = number;
        this.description = description;
        this.isDeleted = false;
        this.role = MemberRole.MANAGER;  // 기본값 설정
    }

    // 저장 전에 기본 role 설정
    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.MANAGER;  // 기본값 설정
        }
    }
}
