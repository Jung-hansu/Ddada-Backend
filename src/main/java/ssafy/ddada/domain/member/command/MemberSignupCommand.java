package ssafy.ddada.domain.member.command;

import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.domain.member.entity.Gender;

import java.time.LocalDate;

public record MemberSignupCommand(
        String nickname,
        String email,
        Gender gender,
        String password,
        LocalDate birth,
        MultipartFile imageUrl,
        String description,
        Integer number
) {

}

