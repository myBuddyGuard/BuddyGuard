package buddyguard.mybuddyguard.walk.controller.response;

import lombok.Builder;

@Builder
public record WalkRecordDetailResponse(
        int count,             // 기록의 총 개수
        Double averageDistance, // 평균 거리 (주간/월간 평균)
        String averageTime      // 평균 산책 시간 (주간/월간 평균)
) {}
