package buddyguard.mybuddyguard.vaccination.repository;

import buddyguard.mybuddyguard.vaccination.entity.VaccinationRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {

    List<VaccinationRecord> findAllByPetId(Long petId);

    Optional<VaccinationRecord> findByIdAndPetId(Long id, Long petId);
}
