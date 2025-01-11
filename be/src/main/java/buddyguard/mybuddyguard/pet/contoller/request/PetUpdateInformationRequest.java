package buddyguard.mybuddyguard.pet.contoller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record PetUpdateInformationRequest(
        String name,
        LocalDate birth
) {

}
