package buddyguard.mybuddyguard.alert.repository;

import buddyguard.mybuddyguard.alert.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findByUserId(Long userId);
}
