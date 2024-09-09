package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ssafy.ddada.common.exception.NotAuthenticatedException;

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

    public static String getLoginMemberRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.isEmpty()) {
            throw new NotAuthenticatedException();
        }

        // 첫 번째 권한을 가져오고 'ROLE_' 접두어 제거
        String role = authorities.iterator().next().getAuthority();

        // ROLE_ 접두어가 있을 경우 제거
        if (role.startsWith("roleROLE_")) {
            role = role.substring(5);
        }

        return role;  // 접두어가 제거된 권한 이름 반환
    }

}
