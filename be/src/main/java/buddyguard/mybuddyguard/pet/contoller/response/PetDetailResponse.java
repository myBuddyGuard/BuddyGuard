package buddyguard.mybuddyguard.pet.contoller.response;

import buddyguard.mybuddyguard.pet.utils.PetType;
import java.time.LocalDate;

public record PetDetailResponse(
        Long userId,
        Long petId,
        String petName,
        String profileImage,
        PetType type,
        LocalDate birth
) {

}
