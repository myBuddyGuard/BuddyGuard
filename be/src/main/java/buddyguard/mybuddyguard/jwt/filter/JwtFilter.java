package buddyguard.mybuddyguard.jwt.filter;

import buddyguard.mybuddyguard.exception.FilterException;
import buddyguard.mybuddyguard.jwt.utils.TokenType;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.jwt.utils.TokenUtility;
import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import buddyguard.mybuddyguard.login.dto.UserDto;
import buddyguard.mybuddyguard.login.exception.NotAccessTokenException;
import buddyguard.mybuddyguard.login.exception.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtFilter(TokenService tokenService, AuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenService = tokenService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 토큰 추출
        String accessToken = TokenUtility.deletePrefixToken(request.getHeader("Authorization"));

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (tokenService.isExpired(accessToken)) {
                // 토큰이 만료된 상황
                throw new TokenExpiredException(HttpStatus.UNAUTHORIZED, "unauthorized token");
            }
            TokenType type = tokenService.getTokenType(accessToken);
            if (!type.equals(TokenType.ACCESS)) {
                // 엑세스 토큰이 아닌 상황
                throw new NotAccessTokenException(HttpStatus.UNAUTHORIZED, "unauthorized token");
            }
        } catch (FilterException exception) {
            authenticationEntryPoint.commence(request, response, exception);
            return;
        }

        Long userId = tokenService.getUserId(accessToken);
        String role = tokenService.getRole(accessToken);
        String name = tokenService.getName(accessToken);

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName(name);
        userDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
