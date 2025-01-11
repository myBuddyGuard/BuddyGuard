package buddyguard.mybuddyguard.weight.repository;

import buddyguard.mybuddyguard.weight.entity.Weight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRepository extends JpaRepository<Weight, Long> {

    List<Weight> findAllByPetId(Long petId);
}
