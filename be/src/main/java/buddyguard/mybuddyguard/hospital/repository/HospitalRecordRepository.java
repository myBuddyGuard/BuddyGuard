package buddyguard.mybuddyguard.hospital.repository;

import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRecordRepository extends JpaRepository<HospitalRecord, Long> {

    List<HospitalRecord> findByPetId(Long petId);

    Optional<HospitalRecord> findByIdAndPetId(Long id, Long petId);

}
