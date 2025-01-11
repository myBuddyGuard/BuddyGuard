package buddyguard.mybuddyguard.walk.repository;

import buddyguard.mybuddyguard.walk.entity.WalkRecordCenterPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkRecordCenterPositionRepository extends JpaRepository<WalkRecordCenterPosition, Long> {

}
