package buddyguard.mybuddyguard.vaccination.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "VACCINATION_RECORDS")
@Entity
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "pet_id")
    private Long petId;

    @Column(nullable = false, name = "vaccination_date")
    private LocalDateTime vaccinationDate;

    @Column(nullable = false, name = "vaccination_name")
    private String vaccinationName;

    @Column(nullable = false, name = "description")
    private String description;

    public void update(LocalDateTime vaccinationDate, String vaccinationName, String description) {
        this.vaccinationDate = vaccinationDate;
        this.vaccinationName = vaccinationName;
        this.description = description;
    }
}
