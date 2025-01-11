package buddyguard.mybuddyguard.alert.service;

import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final UserPetRepository userPetRepository;
    private final AlertTokenService alertTokenService;

    public void sendAlertToAllPetGroup(Long petId, String title, String body) {
        // pet id 로 해당 반려동물 그룹에 속해 있는 모든 user id를 찾는다.
        List<Long> userIds = findUsers(petId);

        List<String> tokens = userIds.stream()
                .map(alertTokenService::getTokenByUserId)
                .toList();

        tokens.forEach(
                token ->sendAlert(token, title, body)
        );
    }

    private List<Long> findUsers(Long petId) {
        List<UserPet> userPets = userPetRepository.getAllByPetId(petId);

        return userPets.stream()
                .map(UserPet::getUser)
                .map(Users::getId)
                .toList();
    }

    public void sendAlert(String fcmToken, String title, String body) {

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            log.info("alert sent!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
