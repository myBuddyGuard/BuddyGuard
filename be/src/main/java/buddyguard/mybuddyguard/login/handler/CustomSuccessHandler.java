package buddyguard.mybuddyguard.login.handler;

import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.jwt.utils.TokenType;
import buddyguard.mybuddyguard.jwt.entity.RefreshToken;
import buddyguard.mybuddyguard.jwt.repository.RefreshTokenRepository;
import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인이 성공하는 경우 동작하는 핸들러 클래스
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final RefreshTokenRepository repository;

    public CustomSuccessHandler(TokenService tokenService, RefreshTokenRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // 성공 후 토큰 생성하기
        CustomOAuth2User oAuth2UserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = oAuth2UserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String refreshToken = tokenService.createJwt(userId, role, TokenType.REFRESH,
                7 * 24 * 60 * 60L);

        // refresh 토큰 저장
        saveRefreshToken(userId, refreshToken, 7 * 24 * 60 * 60L);

        // 쿠키에 토큰 저장
        response.addCookie(createCookie("refresh", refreshToken));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        response.sendRedirect("https://buddyguard.site/");
    }

    /**
     * 쿠키를 생성한다.
     *
     * @param key
     * @param value
     * @return
     */
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void saveRefreshToken(Long userId, String refreshToken, Long expiredSeconds) {
        RefreshToken token = RefreshToken.builder().userId(userId).token(refreshToken)
                .expiration(String.valueOf(Date.from(Instant.now().plusSeconds(expiredSeconds))))
                .build();
        repository.save(token);
    }
}
