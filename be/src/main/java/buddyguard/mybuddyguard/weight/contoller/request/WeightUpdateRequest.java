package buddyguard.mybuddyguard.weight.contoller.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record WeightUpdateRequest(
        @NotNull LocalDateTime recordedAt,
        @NotNull Double weight,
        String description
) {

}
