package buddyguard.mybuddyguard.config;

import buddyguard.mybuddyguard.jwt.filter.JwtFilter;
import buddyguard.mybuddyguard.jwt.repository.RefreshTokenRepository;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.login.filter.CustomLogoutFilter;
import buddyguard.mybuddyguard.login.exception.CustomAuthenticationEntryPoint;
import buddyguard.mybuddyguard.login.handler.CustomSuccessHandler;
import buddyguard.mybuddyguard.login.service.OAuth2UserService;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final TokenService tokenService;
    private final RefreshTokenRepository repository;

    public SecurityConfig(OAuth2UserService oAuth2UserService,
            CustomSuccessHandler customSuccessHandler, TokenService tokenService,
            CustomAuthenticationEntryPoint entryPoint, RefreshTokenRepository repository) {
        this.oAuth2UserService = oAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.entryPoint = entryPoint;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 개발환경 csrf 비활성, 폼로그인이랑 베이직 방식도 쓰지 않을 기능이라 함께 비활성
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(oAuth2UserService))
                .successHandler(customSuccessHandler));

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/", "/login", "/oauth2**", "/swagger-ui/**",
                                "/api-docs/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs",
                                "/reissue", "/error", "/actuator/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated());

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtFilter(tokenService, entryPoint),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new CustomLogoutFilter(tokenService, repository, entryPoint),
                LogoutFilter.class);

        http.oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/authorization/kakao"));

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(Arrays.asList(
                            "https://buddyguard.site:5173",
                            "http://localhost:5173",
                            "https://buddyguard.site",
                            "http://192.168.19.1:5173",
                            "http://172.16.1.38:5173",
                            "http://192.168.55.244:5173"
                    ));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    config.setMaxAge(60 * 60L);

                    return config;
                }));
        return http.build();
    }
}
