package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ssafy.ddada.common.exception.Exception.Security.NotAuthenticatedException;
import ssafy.ddada.domain.member.common.MemberRole;

import java.util.Collection;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class SecurityUtil {

    public static Long getLoginMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof Long)) {
            throw new NotAuthenticatedException();
        }
        return (Long) authentication.getPrincipal();
    }

    public static MemberRole getLoginMemberRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.isEmpty()) {
            throw new NotAuthenticatedException();
        }

        String role = authorities.iterator().next().getAuthority();
        log.info("authorities: {}", authorities);
        log.info("memberRole: {}", role);

        return MemberRole.fromValue(role);
    }
}
