
package ssafy.ddada.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ssafy.ddada.common.exception.NotAuthenticatedException;

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
}


