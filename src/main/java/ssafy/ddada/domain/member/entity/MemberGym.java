package ssafy.ddada.domain.member.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("GROUP")
public class MemberGym extends Member {
    //코트 1:1로 연결 예정
}
