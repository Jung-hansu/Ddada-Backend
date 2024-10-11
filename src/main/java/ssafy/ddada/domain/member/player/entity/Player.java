package ssafy.ddada.domain.member.player.entity;

import jakarta.persistence.*;
import lombok.*;
import ssafy.ddada.common.constant.global.RATING;
import ssafy.ddada.common.constant.global.S3_IMAGE;
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
@ToString
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

    private int winStreak;

    private int loseStreak;

    private int gameCount;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordHistory> passwordHistories = new ArrayList<>();

    @Builder
    public Player(String email, String password, String nickname, String image, String number, Gender gender, LocalDate birth, String description, Integer rating, int winStreak, int loseStreak, int gameCount) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.number = number;
        this.gender = gender;
        this.birth = birth;
        this.description = description;
        this.rating = rating;
        this.winStreak = winStreak;
        this.loseStreak = loseStreak;
        this.gameCount = gameCount;
        this.passwordHistories = new ArrayList<>();
        this.role = MemberRole.PLAYER;
        this.isDeleted = false;
    }

    // 명시적인 생성자 추가 (null 값 허용)
    public Player(Integer rating, MemberRole role) {
        this.rating = rating;
        this.isDeleted = false;
        this.role = role;
        this.loseStreak = 0;
        this.winStreak = 0;
        this.gameCount = 0;
    }

    public static Player createTempPlayer() {
        return new Player(RATING.INITIAL_RATING, MemberRole.TEMP);
    }

    public void signupMember(MemberSignupCommand signupCommand, String imageUrl, String password) {
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
    }

    public void updateProfile(String nickname, String profileImagePath, String description) {
        this.nickname = nickname;
        this.image = profileImagePath;
        this.description = description;
    }

    public void addPasswordHistory(String password) {
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .player(this)
                .password(password)
                .build();
        this.passwordHistories.add(passwordHistory);
    }

    // 비밀번호 변경 전에 이력을 저장
    public void updatePassword(String newPassword) {
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
            image = S3_IMAGE.DEFAULT_URL;
        }
        if (rating == null){
            rating = 800;
        }
    }

    public void incrementStreak(boolean isWin) {
        if (isWin) {
            this.winStreak++;
            this.loseStreak = 0; // 연승이 계속되면 연패는 초기화
        } else {
            this.loseStreak++;
            this.winStreak = 0;
        }
    }

}
