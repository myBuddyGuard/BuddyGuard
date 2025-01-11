package buddyguard.mybuddyguard.jwt.controller;

import buddyguard.mybuddyguard.jwt.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final TokenService service;

    public TokenController(TokenService service) {
        this.service = service;
    }

    @Operation(summary = "액세스 토큰 발급하는 api", description = "리프레시 토큰을 이용하여 액세스 토큰을 새로 발급받습니다")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String newAccess = service.reissueAccessToken(cookies);
        response.setHeader("Authorization", "Bearer " + newAccess);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
