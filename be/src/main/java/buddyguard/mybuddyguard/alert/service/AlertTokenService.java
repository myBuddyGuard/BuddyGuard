package buddyguard.mybuddyguard.alert.service;

import buddyguard.mybuddyguard.alert.entity.UserToken;
import buddyguard.mybuddyguard.alert.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertTokenService {

    private final UserTokenRepository userTokenRepository;

    public void saveToken(Long userId, String fcmToken) {
        userTokenRepository.save(new UserToken(userId, fcmToken));
    }

    public String getTokenByUserId(Long userId) {
        return userTokenRepository
                .findByUserId(userId)
                .getFcmToken();
    }
}
