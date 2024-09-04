package ssafy.ddada.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.member.entity.Gender;
import ssafy.ddada.domain.member.command.MemberSignupCommand;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupRequest{
        @Schema(description = "닉네임", example = "쿠잉비", minimum = "2", maximum = "20")
        @Size(min = 2, max = 20)
        String nickname;

        @Schema(description = "이메일", example = "example@example.com", format = "email")
        @Email(message = "유저ID는 이메일 형식이어야 합니다.")
        String email;

        @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"}, defaultValue = "MALE")
        @NotNull(message = "성별은 필수 입력 값입니다.")
        Gender gender;

        @Schema(description = "프로필 이미지 파일", example = "profile.jpg")
        MultipartFile profileImagePath;

        @Schema(description = "생년월일", example = "1993-01-01")
        LocalDate birth;

        @Schema(description = "비밀번호", example = "password123")
        String password;

        @Schema(description = "핸드폰 번호", example = "010-1234-5678")
        Integer phoneNumber;

        @Schema(description = "자기소개", example = "배드민턴 초보입니다")
        String description;


        public MemberSignupCommand toCommand(String imageUrl) {
                return new MemberSignupCommand(nickname, email, gender, password, birth, imageUrl, description, phoneNumber);
        }
}
