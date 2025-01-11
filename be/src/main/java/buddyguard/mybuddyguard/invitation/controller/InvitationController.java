package buddyguard.mybuddyguard.invitation.controller;

import buddyguard.mybuddyguard.invitation.controller.response.InvitationLinkResponse;
import buddyguard.mybuddyguard.invitation.service.InvitationService;
import buddyguard.mybuddyguard.login.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Operation(summary = "초대 링크를 생성하는 api", description = "펫 id를 이용해서 사용자-펫 그룹 게스트 초대를 위한 초대 링크를 생성합니다")
    @GetMapping("/link/{petId}")
    public ResponseEntity<InvitationLinkResponse> makeInvitationLink(
            @PathVariable("petId") Long petId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        InvitationLinkResponse response = invitationService.makeInvitationLink(userId, petId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "초대 링크를 통해 그룹에 가입하는 api", description = "초대 링크를 통해 유저는 기존 사용자-펫 그룹에 게스트로 가입합니다")
    @GetMapping("/{uuid}")
    public ResponseEntity<Void> registerInvitation(@PathVariable("uuid") String uuid,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getId();
        invitationService.register(uuid, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
