package buddyguard.mybuddyguard.pet.entity;

import buddyguard.mybuddyguard.pet.utils.PetType;
import buddyguard.mybuddyguard.walk.entity.PetWalkRecord;
import buddyguard.mybuddyguard.walk.entity.WalkRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "PETS")
@Getter
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private PetType type;

    private LocalDate birth;

    // 산책 기록과의 N:N 관계
    @OneToMany(mappedBy = "pet")
    private List<PetWalkRecord> petWalkRecords;

    public void update(String name, LocalDate birth) {
        if (name != null) {
            this.name = name;
        }
        if (birth != null) {
            this.birth = birth;
        }
    }

    public void updateProfileImage(String profileImage) {
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }
}
