package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.ddada.domain.member.command.MemberSignupCommand;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
@AllArgsConstructor(access = AccessLevel.PROTECTED)  // 모든 필드를 포함한 생성자 (protected)
public class Member extends BaseEntity implements MemberInterface{

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
    private Integer number;

    @Column(nullable = false)
    private Boolean isDeleted = false;  // 기본값 설정

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = true)
    private String description;

    // 명시적인 생성자 추가 (null 값 허용)
    public Member(String email, Gender gender, LocalDate birth, String nickname, String password, String profileImg, Integer number, String description) {
        this.email = email;
        this.gender = gender;
        this.birth = birth;
        this.nickname = nickname;
        this.password = password;
        this.profileImg = profileImg;
        this.number = number;
        this.description = description;
        this.isDeleted = false;
    }

    public static Member createTempMember(String email) {
        return new Member(
                email,
                null,  // 성별 기본값 설정
                null,  // 생년월일 기본값 설정
                null,  // 닉네임 기본값
                null,  // 임시 비밀번호
                null,  // 프로필 이미지 기본값
                null,  // 전화번호 기본값
                null   // 임시 설명
        );
    }

    public Member signupMember(MemberSignupCommand signupCommand, String imageUrl, String password) {
        this.email = signupCommand.email();
        this.password = password;
        this.nickname = signupCommand.nickname();
        this.profileImg = imageUrl;
        this.number = signupCommand.number();
        this.gender = signupCommand.gender();
        this.birth = signupCommand.birth();
        this.description = signupCommand.description();
        this.isDeleted = false;

        // 현재 객체 (Member) 반환
        return this;
    }

    // 회원 삭제 메서드: 삭제 플래그 설정
    public void deleteMember() {
        this.isDeleted = true;
    }

    public void updateProfile(String nickname, String profileImagePath) {
        this.nickname = nickname;
        this.profileImg = profileImagePath;
    }
}
