package ssafy.ddada.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("관리자"), USER("일반 유저"), TEMP("임시 유저");
    private final String value;
}
