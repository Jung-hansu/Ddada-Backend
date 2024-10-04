package ssafy.ddada.domain.member.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.ddada.domain.member.common.BaseMemberEntity;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.common.Member;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.member.player.command.MemberSignupCommand;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // public 기본 생성자
@AllArgsConstructor(access = AccessLevel.PROTECTED)  // 모든 필드를 포함한 생성자 (protected)
public class Player extends BaseMemberEntity implements Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String image;

    private String number;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private String description;

    private Integer rating;

    private Integer winStreak;

    private Integer loseStreak;

    private Integer gameCount;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordHistory> passwordHistories = new ArrayList<>();

    // 명시적인 생성자 추가 (null 값 허용)
    public Player(String email, Gender gender, LocalDate birth, String nickname, String password, String image, String number, String description, Integer rating, MemberRole role) {
        this.email = email;
        this.gender = gender;
        this.birth = birth;
        this.nickname = nickname;
        this.password = password;
        this.image = image;
        this.number = number;
        this.description = description;
        this.rating = rating;
        this.isDeleted = false;
        this.role = role;
        this.loseStreak = 0;
        this.winStreak = 0;
        this.gameCount = 0;
    }

    public static Player createTempPlayer(String email) {
        return new Player(
                null,
                null,  // 성별 기본값 설정
                null,   // 생년월일 기본값 설정
                null,   // 닉네임 기본값
                null,   // 임시 비밀번호
                null,   // 프로필 이미지 기본값
                null,   // 전화번호 기본값
                null,   // 임시 설명
                800,      // 초기 레이팅
                MemberRole.TEMP
        );
    }

    public Player signupMember(MemberSignupCommand signupCommand, String imageUrl, String password) {
        this.email = signupCommand.email();
        this.password = password;
        this.nickname = signupCommand.nickname();
        this.image = imageUrl;
        this.number = signupCommand.number();
        this.gender = signupCommand.gender();
        this.birth = signupCommand.birth();
        this.description = signupCommand.description();
        this.rating = 800;
        this.isDeleted = false;
        this.role = MemberRole.PLAYER;
        this.gameCount = 0;

        // 현재 객체 (Player) 반환
        return this;
    }

    public void updateProfile(String nickname, String profileImagePath, String description) {
        this.nickname = nickname;
        this.image = profileImagePath;
        this.description = description;
    }

    public void addPasswordHistory(String password) {
        PasswordHistory passwordHistory = new PasswordHistory(this, password);
        this.passwordHistories.add(passwordHistory);
    }

    public void updatePassword(String newPassword) {
        // 비밀번호 변경 전에 이력을 저장
        this.addPasswordHistory(this.password);
        this.password = newPassword;
    }

    // 저장 전에 기본 role 설정
    @Override
    public void prePersist() {
        if (this.role == null) {
            this.role = MemberRole.PLAYER;  // 기본값 설정
        }
        if (image == null){
            image = "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg";
        }
        if (rating == null){
            rating = 800;
        }
    }

    public void incrementWinStreak() {
        this.winStreak++;
        this.loseStreak = 0; // 연승이 계속되면 연패는 초기화
    }

    public void incrementLoseStreak() {
        this.loseStreak++;
        this.winStreak = 0; // 연패가 계속되면 연승은 초기화
    }
}
