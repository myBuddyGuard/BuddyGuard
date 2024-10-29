package buddyguard.mybuddyguard.hospital.controller.request;

import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
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
        public static HospitalRecord toHospitalRecord(Long petId, HospitalRecordCreateRequest request) {
                return HospitalRecord.builder()
                        .petId(petId)
                        .date(request.date())
                        .title(request.title())
                        .description(request.description())
                        .build();
        }

}

