package buddyguard.mybuddyguard.hospital.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record HospitalRecordUpdateRequest(

        @NotNull
        LocalDateTime date,
        @NotBlank
        String title,
        @NotBlank
        String description
) {

}
