package buddyguard.mybuddyguard.alert.controller;

import buddyguard.mybuddyguard.alert.controller.request.AlertTokenRequest;
import buddyguard.mybuddyguard.alert.service.AlertTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class AlertTokenController {

    private final AlertTokenService alertTokenService;

    @PostMapping
    public ResponseEntity<Void> saveToken(@RequestBody AlertTokenRequest alertTokenRequest) {

        alertTokenService.saveToken(
                alertTokenRequest.userId(),
                alertTokenRequest.fcmToken()
        );

        return ResponseEntity.ok().build();
    }
}
