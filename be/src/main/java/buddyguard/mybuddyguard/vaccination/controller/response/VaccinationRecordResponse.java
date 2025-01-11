package buddyguard.mybuddyguard.vaccination.controller.response;

import java.time.LocalDateTime;

public record VaccinationRecordResponse(
        Long id,
        Long petId,
        LocalDateTime vaccinationDate,
        String vaccinationName,
        String mainCategory,
        String description
) {

}
