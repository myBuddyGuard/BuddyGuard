package buddyguard.mybuddyguard.weight.contoller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record WeightCreateRequest(
        @NotNull Long petId,
        @NotNull LocalDateTime recordedAt,
        @NotNull @Positive Double weight,
        String description // 필수값이 아니지..?
) {

}
