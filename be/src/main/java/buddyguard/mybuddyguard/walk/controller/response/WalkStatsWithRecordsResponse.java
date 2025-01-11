package buddyguard.mybuddyguard.walk.controller.response;

import java.util.List;
import lombok.Builder;

@Builder
public record WalkStatsWithRecordsResponse(
        WalkRecordDetailResponse stats,               // 통계 정보
        List<WalkRecordResponse> records        // 개별 산책 기록
) {}
