package ssafy.ddada.domain.member.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.common.MemberInterface;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
@AllArgsConstructor(access = AccessLevel.PROTECTED)  // 모든 필드를 포함한 생성자 (protected)
public class Player extends BaseMemberEntity implements MemberInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String profileImg;

    private String number;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private String description;

    private Integer rating;

    // 명시적인 생성자 추가 (null 값 허용)
    public Player(String email, Gender gender, LocalDate birth, String nickname, String password, String profileImg, String number, String description, Integer rating, MemberRole role) {
        this.email = email;
        this.gender = gender;
        this.birth = birth;
        this.nickname = nickname;
        this.password = password;
        this.profileImg = profileImg;
        this.number = number;
        this.description = description;
        this.rating = rating;
        this.isDeleted = false;
        this.role = role;
    }

    public static Player createTempMember(String email) {
        return new Player(
                email,
                null,  // 성별 기본값 설정
                null,   // 생년월일 기본값 설정
                null,   // 닉네임 기본값
                null,   // 임시 비밀번호
                null,   // 프로필 이미지 기본값
                null,   // 전화번호 기본값
                null,   // 임시 설명
                0,      // 초기 레이팅
                MemberRole.TEMP
        );
    }

    public Player signupMember(MemberSignupCommand signupCommand, String imageUrl, String password) {
        this.email = signupCommand.email();
        this.password = password;
        this.nickname = signupCommand.nickname();
        this.profileImg = imageUrl;
        this.number = signupCommand.number();
        this.gender = signupCommand.gender();
        this.birth = signupCommand.birth();
        this.description = signupCommand.description();
        this.rating = 0;
        this.isDeleted = false;
        this.role = MemberRole.PLAYER;

        // 현재 객체 (Player) 반환
        return this;
    }

    public void updateProfile(String nickname, String profileImagePath) {
        this.nickname = nickname;
        this.profileImg = profileImagePath;
    }

    // 저장 전에 기본 role 설정
    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.PLAYER;  // 기본값 설정
        }
    }
}
