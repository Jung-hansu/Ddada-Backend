package ssafy.ddada.domain.manager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ddada.domain.member.entity.MemberInterface;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
public class Manager extends BaseManagerEntity implements MemberInterface {
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

    @Column(nullable = true)
    private String profileImg;

    @Column(nullable = true)
    private String number;

    @Column(nullable = false)
    private Boolean isDeleted;

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
    }

    // 매니저 삭제 메서드
    public void deleteManager() {
        this.isDeleted = true;
    }
}
