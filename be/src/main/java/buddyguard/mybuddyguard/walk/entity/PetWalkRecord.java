package buddyguard.mybuddyguard.walk.entity;

import buddyguard.mybuddyguard.pet.entity.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "PET_WALK_RECORDS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetWalkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_record_id", nullable = false , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WalkRecord walkRecord;

    public Long getPetId() {
        return this.pet.getId();
    }
}
