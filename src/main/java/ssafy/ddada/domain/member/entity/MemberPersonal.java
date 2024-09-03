package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PERSONAL")
public class MemberPersonal extends Member {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = true)
    private String description;
}
