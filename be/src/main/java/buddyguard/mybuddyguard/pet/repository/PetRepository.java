package buddyguard.mybuddyguard.pet.repository;

import buddyguard.mybuddyguard.pet.entity.Pet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findById(Long id);
}
