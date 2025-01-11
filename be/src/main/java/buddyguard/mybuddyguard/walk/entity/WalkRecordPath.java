package buddyguard.mybuddyguard.walk.entity;

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
@Table(name = "WALK_RECORD_PATH")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WalkRecordPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 경로의 위도
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    // 경로의 경도
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    // WalkRecord와의 ManyToOne 관계 설정 (단방향 1:N 관계의 N쪽)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_record_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private WalkRecord walkRecord;
    

}
