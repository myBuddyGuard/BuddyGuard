package buddyguard.mybuddyguard.jwt.service;

import buddyguard.mybuddyguard.jwt.utils.TokenType;
import buddyguard.mybuddyguard.jwt.repository.RefreshTokenRepository;
import buddyguard.mybuddyguard.jwt.utils.TokenUtility;
import buddyguard.mybuddyguard.login.exception.NotAccessTokenException;
import buddyguard.mybuddyguard.login.exception.TokenExpiredException;
import buddyguard.mybuddyguard.login.exception.TokenNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰과 관련된 작업들을 진행하는 클래스
 */
@Component
public class TokenService {

    private final SecretKey secretKey;
    private final RefreshTokenRepository repository;

    public TokenService(@Value("${spring.jwt.secret}") String secret,
            RefreshTokenRepository repository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SIG.HS256.key().build().getAlgorithm());
        this.repository = repository;
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("userId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    public String getName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("name", String.class);
    }

    /**
     * 토큰의 유효기간이 만료되었는지 확인합니다
     *
     * @param token
     * @return
     */
    public Boolean isExpired(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public TokenType getTokenType(String token) {
        String stringTokens = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload()
                .get("tokenType", String.class);
        return TokenType.valueOf(stringTokens);
    }

    /**
     * JWT 토큰을 생성합니다
     *
     * @param userId
     * @param role
     * @param expiredSeconds
     * @return
     */
    public String createJwt(Long userId, String role, TokenType tokenType, Long expiredSeconds) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .claim("tokenType", tokenType)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expiredSeconds)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 엑세스 토큰을 발급합니다.
     *
     * @param cookies
     * @return
     */
    public String reissueAccessToken(Cookie[] cookies) {
        String refresh = TokenUtility.getRefreshToken(cookies);

        if (refresh == null) {
            throw new TokenNotFoundException(HttpStatus.UNAUTHORIZED, "unauthorized token");
        }
        // 만료 확인
        if (isExpired(refresh)) {
            throw new TokenExpiredException(HttpStatus.UNAUTHORIZED, "unauthorized token");
        }
        // 리프레시 확인
        TokenType tokenType = getTokenType(refresh);
        if (tokenType != TokenType.REFRESH) {
            throw new NotAccessTokenException(HttpStatus.UNAUTHORIZED, "unauthorized token");
        }
        // DB와 비교
        if (!repository.existsById(refresh)) {
            throw new TokenNotFoundException(HttpStatus.UNAUTHORIZED, "unauthorized token");
        }

        Long userId = getUserId(refresh);
        String userRole = getRole(refresh);

        return createJwt(userId, userRole, TokenType.ACCESS, 1000000000 * 60L);
    }
}
