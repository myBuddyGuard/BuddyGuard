package buddyguard.mybuddyguard.walk.controller.response;

import buddyguard.mybuddyguard.walk.entity.WalkRecordCenterPosition;
import buddyguard.mybuddyguard.walk.entity.WalkRecordPath;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record WalkRecordResponse(
        Long id,
        List<Long> buddyIds,
        LocalDate startDate,   // 산책 시작 날짜
        LocalDate endDate,     // 산책 종료 날짜
        String startTime,   // 산책 시작 시간
        String endTime,     // 산책 종료 시간
        String totalTime,      // 총 산책 시간 (00:00:46 형식의 문자열)
        String note,           // 산책에 대한 메모
        WalkRecordPosition centerPosition, // 중심 위치 (위도, 경도)
        Integer mapLevel,      // 지도 레벨
        List<WalkRecordPosition> path,           // 산책 경로 (위도, 경도 배열)
        Double distance,                 // 총 거리
        String fileUrl
) {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class WalkRecordPosition {

        @NotNull
        private final Double latitude;  // 위도
        @NotNull
        private final Double longitude; // 경도

        public static WalkRecordPosition fromWalkRecordCenterPosition(WalkRecordCenterPosition entity) {
            return WalkRecordPosition.builder()
                    .latitude(entity.getLatitude())
                    .longitude(entity.getLongitude())
                    .build();
        }

        public static WalkRecordPosition fromWalkRecordPath(WalkRecordPath entity) {
            return WalkRecordPosition.builder()
                    .latitude(entity.getLatitude())
                    .longitude(entity.getLongitude())
                    .build();
        }

    }
}
