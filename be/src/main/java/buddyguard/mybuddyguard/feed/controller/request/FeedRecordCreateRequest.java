package buddyguard.mybuddyguard.feed.controller.request;

import buddyguard.mybuddyguard.feed.entity.FeedRecord;
import buddyguard.mybuddyguard.feed.utils.AmountType;
import buddyguard.mybuddyguard.feed.utils.FeedType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record FeedRecordCreateRequest(
        @NotNull LocalDateTime date,
        @NotNull Long amount,
        @NotNull @JsonProperty("amount_type") AmountType amountType,
        @NotNull @JsonProperty("feed_type") FeedType feedType
) {

    public FeedRecord toEntity(Long petId) {
        return FeedRecord.builder()
                .petId(petId)
                .date(this.date)
                .amount(this.amount)
                .amountType(this.amountType)
                .feedType(this.feedType)
                .build();
    }
}
