package ssafy.ddada.domain.member.command;

import org.springframework.cglib.core.Local;
import ssafy.ddada.domain.member.entity.Gender;

import java.time.LocalDate;

public record MemberSignupCommand(
        String nickName,
        String email,
        Gender gender,
        Integer age,
        String interest,
        String password,
        LocalDate birth,
        String  imageUrl
) {

}

