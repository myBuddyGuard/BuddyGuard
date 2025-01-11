package buddyguard.mybuddyguard.feed.controller.response;

import buddyguard.mybuddyguard.feed.utils.AmountType;
import buddyguard.mybuddyguard.feed.utils.FeedType;
import java.time.LocalDateTime;

public record FeedRecordResponse(
        Long id,
        Long perId,
        FeedType feedType,
        LocalDateTime date,
        String mainCategory,
        String subCategory,
        Long amount,
        AmountType amountType
) {

}
