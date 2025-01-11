package buddyguard.mybuddyguard.login.controller;

import buddyguard.mybuddyguard.login.controller.response.UserInformationResponse;
import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import buddyguard.mybuddyguard.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "로그인 한 유저의 정보를 가져오는 api", description = "유저 이름, 이메일, 프로필 사진, 펫들 아이디를 포함한 정보를 가져옵니다.")
    @GetMapping("/user")
    public ResponseEntity<UserInformationResponse> getUserInformation(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        UserInformationResponse result = userService.getUserInformation(userId);
        return ResponseEntity.ok(result);
    }
}
