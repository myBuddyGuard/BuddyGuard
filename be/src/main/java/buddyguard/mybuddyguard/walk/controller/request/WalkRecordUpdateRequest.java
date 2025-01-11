package buddyguard.mybuddyguard.walk.controller.request;

import buddyguard.mybuddyguard.walk.entity.WalkRecord;
import buddyguard.mybuddyguard.walk.entity.WalkRecordCenterPosition;
import buddyguard.mybuddyguard.walk.entity.WalkRecordPath;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record WalkRecordUpdateRequest(
        List<Long> buddysId,       // 추가된 반려동물들의 ID 배열 (문자열)
        LocalDate startDate,   // 산책 시작 날짜
        LocalDate endDate,     // 산책 종료 날짜
        String startTime,   // 산책 시작 시간
        String endTime,     // 산책 종료 시간
        String totalTime,      // 총 산책 시간 (00:00:46 형식의 문자열)
        String note,           // 산책에 대한 메모
        WalkRecordPathRequest centerPosition, // 중심 위치 (위도, 경도)
        Integer mapLevel,      // 지도 레벨
        List<WalkRecordPathRequest>  path,           // 산책 경로 (위도, 경도 배열)
        Double distance
) {
        public void updateWalkRecord(WalkRecord existingRecord) {
                // 필요한 필드만 업데이트 (centerPosition, path는 유지)
                WalkRecord updatedRecord = WalkRecord.builder()
                        .id(existingRecord.getId())  // 기존 ID 유지
                        .startDate(this.startDate() != null ? this.startDate() : existingRecord.getStartDate())  // startDate 업데이트
                        .endDate(this.endDate() != null ? this.endDate() : existingRecord.getEndDate())          // endDate 업데이트
                        .endTime(this.endTime() != null ? this.endTime() : existingRecord.getEndTime())          // endTime 업데이트
                        .totalTime(this.totalTime() != null ? this.totalTime() : existingRecord.getTotalTime())  // totalTime 업데이트
                        .note(this.note() != null ? this.note() : existingRecord.getNote())                      // note 업데이트
                        .distance(this.distance() != null ? this.distance() : existingRecord.getDistance())      // distance 업데이트
                        .centerPosition(existingRecord.getCenterPosition())  // 기존 centerPosition 유지
                        .mapLevel(existingRecord.getMapLevel())  // 기존 mapLevel 유지
                        .path(new ArrayList<>(existingRecord.getPath()))  // 기존 path 유지
                        .pathImage(existingRecord.getPathImage())  // 기존 pathImage 유지
                        .petWalkRecords(existingRecord.getPetWalkRecords())  // 기존 petWalkRecords 유지
                        .build();

                // 기존 엔티티의 필드 값을 새 엔티티로 교체 (엔티티 교체)
                existingRecord.replaceWith(updatedRecord);
        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor(force = true)
        @Builder
        public static class WalkRecordPathRequest {

                @NotNull
                private final Double latitude;  // 위도
                @NotNull
                private final Double longitude; // 경도

                public WalkRecordPath toWalkRecordPath() {
                        return WalkRecordPath.builder()
                                .latitude(this.latitude)
                                .longitude(this.longitude)
                                .build();
                }

                public WalkRecordCenterPosition toWalkRecordCenterPosition() {
                        return WalkRecordCenterPosition.builder()
                                .latitude(this.latitude)
                                .longitude(this.longitude)
                                .build();
                }

        }
}
