package buddyguard.mybuddyguard.walk.entity;

import buddyguard.mybuddyguard.walkimage.entity.WalkS3Image;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "WALK_RECORD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class WalkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 산책에 참여한 여러 마리의 펫 (N:N 관계 설정)
    @OneToMany(mappedBy = "walkRecord", fetch = FetchType.LAZY)
    private List<PetWalkRecord> petWalkRecords;

    // 산책 시작 날짜 (예: 2024년 10월 7일)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // 산책 종료 날짜 (예: 2024년 10월 7일)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // 산책 시작 시간 (예: 23:40)
    @Column(name = "start_time", nullable = false)
    private String startTime;

    // 산책 종료 시간 (예: 23:41)
    @Column(name = "end_time", nullable = false)
    private String endTime;

    // 총 산책 시간 (예: 00:00:46)
    @Column(name = "total_time", nullable = false)
    private String totalTime;

    // 산책에 대한 메모
    @Column(name = "note", nullable = false)
    private String note;

    // 산책 경로의 중심 위치 (위도, 경도), JSON 문자열로 저장
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_position_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WalkRecordCenterPosition centerPosition;

    // 지도 레벨
    @Column(name = "map_level", nullable = false)
    private Integer mapLevel;

    @OneToMany(mappedBy = "walkRecord", fetch = FetchType.LAZY)
    private List<WalkRecordPath> path;

    // 산책 경로 이미지를 파일 경로로 저장 (이미지 업로드는 별도의 로직에서 처리)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_image_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WalkS3Image pathImage;

    // 총 거리 (km), 최대 소수점 3자리까지
    @Column(name = "distance", nullable = false)
    private Double distance;

    public boolean hasBuddy(long petId) {
        return petWalkRecords.stream()
                .anyMatch(petWalkRecord -> petWalkRecord.getPetId().equals(petId));
    }

    public void replaceWith(WalkRecord updatedRecord) {
        // startDate, endDate, endTime, totalTime, note, distance 만 업데이트
        this.startDate = updatedRecord.getStartDate();
        this.endDate = updatedRecord.getEndDate();
        this.endTime = updatedRecord.getEndTime();
        this.totalTime = updatedRecord.getTotalTime();
        this.note = updatedRecord.getNote();
        this.distance = updatedRecord.getDistance();
    }

}
