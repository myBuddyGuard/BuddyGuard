package buddyguard.mybuddyguard.walk.repository;

import buddyguard.mybuddyguard.walk.entity.WalkRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    Optional<WalkRecord> findById(Long id);

}
