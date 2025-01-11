package buddyguard.mybuddyguard.login.repository;

import buddyguard.mybuddyguard.login.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByOauthId(String oAuthId);

    boolean existsById(Long id);
}
