package buddyguard.mybuddyguard.hospital.controller.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record HospitalRecordCreateRequest(

        @NotNull
        Long userId,
        @NotNull
        Long petId,
        @NotNull
        LocalDateTime visitDate,
        @NotNull
        String hospitalName,
        @NotNull
        String description
) {

}