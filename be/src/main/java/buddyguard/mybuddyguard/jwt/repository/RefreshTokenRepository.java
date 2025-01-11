package buddyguard.mybuddyguard.jwt.repository;

import buddyguard.mybuddyguard.jwt.entity.RefreshToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends KeyValueRepository<RefreshToken, String> {

}
