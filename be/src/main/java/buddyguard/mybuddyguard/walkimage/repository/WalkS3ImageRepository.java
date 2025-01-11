package buddyguard.mybuddyguard.walkimage.repository;

import buddyguard.mybuddyguard.walkimage.entity.WalkS3Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkS3ImageRepository extends JpaRepository<WalkS3Image, Long> {
}
