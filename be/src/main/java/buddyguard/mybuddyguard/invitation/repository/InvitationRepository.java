package buddyguard.mybuddyguard.invitation.repository;

import buddyguard.mybuddyguard.invitation.entity.InvitationInformation;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends KeyValueRepository<InvitationInformation, String> {
    boolean existsById(String id);
}
