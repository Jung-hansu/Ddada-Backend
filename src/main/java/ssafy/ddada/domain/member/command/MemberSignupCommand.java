package ssafy.ddada.domain.member.command;

import ssafy.ddada.domain.member.entity.Gender;

import java.time.LocalDate;

public record MemberSignupCommand(
        String nickname,
        String email,
        Gender gender,
        String password,
        LocalDate birth,
        String  imageUrl,
        String description,
        Integer number
) {

}

