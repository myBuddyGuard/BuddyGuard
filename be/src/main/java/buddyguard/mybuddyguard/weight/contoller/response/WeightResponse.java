package buddyguard.mybuddyguard.weight.contoller.response;

import java.time.LocalDateTime;

public record WeightResponse(
        Long id,
        Long petId,
        Double weight,
        LocalDateTime recordedAt,
        String mainCategory,
        String description
) {

}
