package buddyguard.mybuddyguard.vaccination.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VaccinationRecordUpdateRequest(
        @NotNull LocalDateTime vaccinationDate,
        @NotBlank String vaccinationName,
        @NotBlank String description
) {

}
