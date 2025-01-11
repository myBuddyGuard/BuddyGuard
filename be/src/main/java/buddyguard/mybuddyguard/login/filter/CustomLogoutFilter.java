package buddyguard.mybuddyguard.login.filter;

import buddyguard.mybuddyguard.exception.FilterException;
import buddyguard.mybuddyguard.jwt.utils.TokenType;
import buddyguard.mybuddyguard.jwt.repository.RefreshTokenRepository;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.jwt.utils.TokenUtility;
import buddyguard.mybuddyguard.login.exception.CustomAuthenticationEntryPoint;
import buddyguard.mybuddyguard.login.exception.LogoutException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

public class CustomLogoutFilter extends GenericFilterBean {

    private final TokenService service;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final RefreshTokenRepository repository;

    public CustomLogoutFilter(TokenService service, RefreshTokenRepository repository
            , CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.service = service;
        this.repository = repository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        // POST 요청으로 온 logout 요청이 아닌 경우는 로그아웃 필터 통과
        String uri = request.getRequestURI();
        if (!uri.matches("/logout$")) {
            chain.doFilter(request, response);
            return;
        }
        String method = request.getMethod();
        if (!method.equals("POST")) {
            chain.doFilter(request, response);
            return;
        }

        String refresh = TokenUtility.getRefreshToken(request.getCookies());
        try {
            if (refresh == null) {
                throw new LogoutException(HttpStatus.BAD_REQUEST, "token not found");
            }
            if (service.isExpired(refresh)) {
                throw new LogoutException(HttpStatus.BAD_REQUEST, "already logout");
            }
            if (!repository.existsById(refresh)) {
                throw new LogoutException(HttpStatus.BAD_REQUEST, "token not exist");
            }
            if (!service.getTokenType(refresh).equals(TokenType.REFRESH)) {
                throw new LogoutException(HttpStatus.BAD_REQUEST, "not refresh token");
            }
        } catch (FilterException exception) {
            authenticationEntryPoint.commence(request, response, exception);
            return;
        }

        // 로그아웃
        repository.deleteById(refresh);

        // 카카오 api에 로그아웃 요청 ?
        chain.doFilter(request, response);
    }
}
