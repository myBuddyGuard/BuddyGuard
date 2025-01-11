package buddyguard.mybuddyguard.weight.entity;

import buddyguard.mybuddyguard.exception.InvalidRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "WEIGHT_RECORDS")
@ToString
@Getter
public class Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    private Double weight;

    private String description;

    public Weight(Long petId, LocalDateTime recordedAt, Double weight, String description) {
        this.petId = petId;
        this.recordedAt = recordedAt;
        this.weight = weight;
        this.description = description;
    }

    public void update(LocalDateTime recordedAt, Double weight, String description) { // 변경 포인트를 해당 메서드에 모아서 하나로 통일한다.
        this.recordedAt = recordedAt;
        this.weight = weight;
        this.description = description;
    }

    public void validateOwnership(Long petId) {
        if (!this.petId.equals(petId)) {
            throw new InvalidRequestException();
        }
    }
}
