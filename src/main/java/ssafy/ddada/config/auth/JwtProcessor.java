package ssafy.ddada.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ssafy.ddada.common.error.exception.security.TokenExpiredException;
import ssafy.ddada.common.error.exception.security.InvalidSignatureTokenException;
import ssafy.ddada.common.error.exception.security.InvalidTokenException;
import ssafy.ddada.common.error.exception.token.TokenTypeNotMatchedException;
import ssafy.ddada.common.properties.JwtProperties;
import ssafy.ddada.domain.auth.model.LoginTokenModel;
import ssafy.ddada.domain.member.common.MemberInterface;
import ssafy.ddada.domain.member.common.MemberRole;
import ssafy.ddada.domain.redis.BlacklistTokenRedisRepository;
import ssafy.ddada.domain.redis.RefreshTokenRedisRepository;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import static ssafy.ddada.common.constant.redis.KEY_PREFIX.ACCESS_TOKEN;
import static ssafy.ddada.common.constant.redis.KEY_PREFIX.REFRESH_TOKEN;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProcessor {

    private final JwtProperties jwtProperties;
    private final BlacklistTokenRedisRepository blacklistTokenRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
    }

    public Jws<Claims> getClaim(String token) {
        log.debug("token : {}", token);
        if (isTokenExpired(token)) {
            throw new TokenExpiredException();
        }
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getSecretKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (SignatureException e) {
            throw new InvalidSignatureTokenException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public void saveRefreshToken(String accessToken, String refreshToken) {
        refreshTokenRedisRepository.save(accessToken, refreshToken);
    }

    public void saveRefreshToken(LoginTokenModel tokens) {
        refreshTokenRedisRepository.save(tokens.accessToken(), tokens.refreshToken());
    }

    public String findRefreshTokenById(String accessToken) {
        return refreshTokenRedisRepository.findById(accessToken)
                .orElseThrow(InvalidTokenException::new);
    }

    public void renewRefreshToken(String oldRefreshToken, String newRefreshToken, MemberInterface member) {
        refreshTokenRedisRepository.save(newRefreshToken, String.valueOf(member.getId()));
        expireToken(oldRefreshToken);
    }

    public void expireToken(String refreshToken) {
        blacklistTokenRedisRepository.save(refreshToken, getRemainingTime(refreshToken));
        refreshTokenRedisRepository.delete(refreshToken);
        log.info("Token added to blacklist: {}", refreshToken);
    }

    public long getRemainingTime(String token) {
        Claims claims = getClaim(token).getPayload();
        Date expiration = claims.getExpiration();
        Date now = new Date();
        return Math.max(0, expiration.getTime() - now.getTime());
    }

    public boolean isTokenExpired(String token) {
        Boolean result = blacklistTokenRedisRepository.hasKey(token);
        if (result != null) {
            return result;
        }
        return getClaim(token).getPayload().getExpiration().before(new Date());
    }

    public String generateAccessToken(MemberInterface member) {
        log.debug("access token exp : {}", jwtProperties.accessTokenExp());
        return issueToken(member.getId(), member.getRole(), ACCESS_TOKEN, jwtProperties.accessTokenExp());
    }

    public String generateRefreshToken(MemberInterface member) {
        return issueToken(member.getId(), member.getRole(), REFRESH_TOKEN, jwtProperties.refreshTokenExp());
    }

    public DecodedJwtToken decodeToken(String token, String type) {
        Claims claims = getClaim(token).getPayload();
        checkType(claims, type);

        return new DecodedJwtToken(
                Long.valueOf(claims.getSubject()),
                String.valueOf(claims.get("role")),
                String.valueOf(claims.get("type"))
        );
    }

    private String issueToken(Long userId, MemberRole role, String type, Long time) {
        Date now = new Date();
        return Jwts.builder()
                .issuer("Cooing Inc.")
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + time))
                .claim("type", type)
                .claim("role", role.getValue())
                .signWith(getSecretKey())
                .compact();
    }

    private void checkType(Claims claims, String type) {
        if (!type.equals(String.valueOf(claims.get("type")))) {
            throw new TokenTypeNotMatchedException();
        }
    }
}
