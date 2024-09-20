package ssafy.ddada.domain.member.common;

public interface Member {
    Long getId();
    MemberRole getRole();
    String getPassword();
}
