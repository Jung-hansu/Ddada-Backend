package ssafy.ddada.domain.member.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    PLAYER("선수"), MANAGER("매니저"), GYM_ADMIN("체육관 관리자"), TEMP("임시 유저");
    private final String value;


    public static MemberRole fromValue(String role) {
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        for (MemberRole memberRole : MemberRole.values()) {
            if (memberRole.name().equalsIgnoreCase(role) || memberRole.getValue().equals(role)) {
                return memberRole;
            }
        }
        throw new IllegalArgumentException("해당하는 회원 역할이 없습니다: " + role);
    }
}