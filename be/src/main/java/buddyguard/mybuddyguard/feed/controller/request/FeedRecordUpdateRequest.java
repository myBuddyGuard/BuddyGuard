package buddyguard.mybuddyguard.feed.controller.request;

import buddyguard.mybuddyguard.feed.utils.AmountType;
import buddyguard.mybuddyguard.feed.utils.FeedType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record FeedRecordUpdateRequest(
        LocalDateTime date,
        Long amount,
        @JsonProperty("amount_type") AmountType amountType,
        @JsonProperty("feed_type") FeedType feedType
) {

}
