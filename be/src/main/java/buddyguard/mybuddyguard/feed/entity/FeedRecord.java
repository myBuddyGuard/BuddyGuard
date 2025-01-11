package buddyguard.mybuddyguard.feed.entity;

import buddyguard.mybuddyguard.feed.controller.response.FeedRecordResponse;
import buddyguard.mybuddyguard.feed.utils.AmountType;
import buddyguard.mybuddyguard.feed.utils.FeedType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "FEED_RECORDS")
@Getter
@Builder
public class FeedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pet_id")
    private Long petId;
    private LocalDateTime date;
    private Long amount;
    @Column(name = "amount_type")
    @Enumerated(EnumType.STRING)
    private AmountType amountType;
    @Column(name = "feed_type")
    @Enumerated(EnumType.STRING)
    private FeedType feedType;

    public FeedRecordResponse toDto() {
        return new FeedRecordResponse(
                this.id,
                this.petId,
                this.feedType,
                this.date,
                "FEED",
                this.feedType.name(),
                this.amount,
                this.amountType
        );
    }

    public void update(Long amount, AmountType amountType, FeedType feedType,
            LocalDateTime date) {
        if (amount != null) {
            this.amount = amount;
        }
        if (amountType != null) {
            this.amountType = amountType;
        }
        if (feedType != null) {
            this.feedType = feedType;
        }
        if (date != null) {
            this.date = date;
        }
    }
}
