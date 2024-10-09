package buddyguard.mybuddyguard.pet.repository;

import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.utils.UserPetRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPetRepository extends JpaRepository<UserPet, Long> {
    @Query("SELECT count(*) >= 3 FROM UserPet u where u.id = :userId")
    boolean existsByUserExceedPetCount(@Param("userId") Long userId);

    List<UserPet> findByUserId(Long userId);

    Optional<UserPet> findByUserIdAndPetId(Long userId, Long petId);

    List<UserPet> getAllByPetId(Long petId);

    boolean existsByUserIdAndPetId(Long userId, Long petId);

    boolean existsByUserIdAndPetIdAndRole(Long userId, Long petId, UserPetRole role);

    boolean existsByPetIdAndRole(Long petId, UserPetRole role);
}
