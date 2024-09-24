package ssafy.ddada.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ssafy.ddada.config.auth.DecodedJwtToken;
import ssafy.ddada.config.auth.JwtProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProcessor jwtProcessor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (Objects.nonNull(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContextHolder에 담기
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        DecodedJwtToken decodedJwtToken = jwtProcessor.decodeToken(token, ACCESS_TOKEN);

        Long memberId = decodedJwtToken.memberId();
        String role = decodedJwtToken.role();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(memberId, null, authorities);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else if (bearerToken != null && !bearerToken.isBlank()) {
            return bearerToken;
        }
        return null;
    }
}
