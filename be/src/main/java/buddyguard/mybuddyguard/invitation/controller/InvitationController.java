package buddyguard.mybuddyguard.invitation.controller;

import buddyguard.mybuddyguard.invitation.controller.response.InvitationLinkResponse;
import buddyguard.mybuddyguard.invitation.service.InvitationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{userId}/{petId}")
    public ResponseEntity<InvitationLinkResponse> makeInvitationLink(
            @PathVariable("userId") Long userId, @PathVariable("petId") Long petId) {
        InvitationLinkResponse response = invitationService.makeInvitationLink(userId, petId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Void> registerInvitation(@PathVariable("uuid") String uuid,
            HttpServletRequest request) {
        String token = request.getHeader("access");
        invitationService.register(uuid, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
