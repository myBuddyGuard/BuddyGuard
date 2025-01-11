package buddyguard.mybuddyguard.hospital.controller.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record HospitalRecordCreateRequest(

        @NotNull
        Long petId,
        @NotNull
        LocalDateTime date,
        @NotNull
        String title,
        @NotNull
        String description

) {

}

