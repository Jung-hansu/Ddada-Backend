package ssafy.ddada.domain.member.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    PLAYER("선수"), MANAGER("매니저"), COURT_ADMIN("시설 관리자"), TEMP("임시 유저");
    private final String value;
}
