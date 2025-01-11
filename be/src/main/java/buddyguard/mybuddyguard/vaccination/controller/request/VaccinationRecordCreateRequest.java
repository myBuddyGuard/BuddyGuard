package buddyguard.mybuddyguard.vaccination.controller.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VaccinationRecordCreateRequest(
        @NotNull Long petId,
        @NotNull LocalDateTime vaccinationDate,
        @NotNull String vaccinationName,
        @NotNull String description
) {

}
