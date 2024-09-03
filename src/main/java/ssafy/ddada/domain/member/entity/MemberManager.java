package ssafy.ddada.domain.member.entity;

import jakarta.persistence.Column;

public class MemberManager {
    @Column(nullable = false)
    private String description;
}
