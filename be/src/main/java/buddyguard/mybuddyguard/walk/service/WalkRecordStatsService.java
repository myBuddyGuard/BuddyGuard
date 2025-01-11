package buddyguard.mybuddyguard.walk.service;

import buddyguard.mybuddyguard.walk.controller.response.WalkRecordDetailResponse;
import buddyguard.mybuddyguard.walk.controller.response.WalkRecordResponse;
import buddyguard.mybuddyguard.walk.controller.response.WalkStatsWithRecordsResponse;
import buddyguard.mybuddyguard.walk.entity.WalkRecord;
import buddyguard.mybuddyguard.walk.mapper.WalkRecordMapper;
import buddyguard.mybuddyguard.walk.repository.WalkRecordRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkRecordStatsService {

    private final WalkRecordRepository walkRecordRepository;

    // 특정 반려동물의 주간 산책 기록을 조회하고 통계와 상세기록 응답
    public WalkStatsWithRecordsResponse getWeeklyRecords(Long petId, LocalDate requestedDate) {
        //요청된 날짜 없으면 오늘 날짜 사용
        if (requestedDate == null) {
            requestedDate = LocalDate.now();
        }

        // 현재 주의 첫 번째 날 (일요일)과 마지막 날 (토요일) 계산
        WeekFields weekFields = WeekFields.of(Locale.KOREA); // 한국 기준으로 주 시작일 설정
        LocalDate startOfWeek = requestedDate.with(weekFields.dayOfWeek(), 1); // 일요일 시작
        LocalDate endOfWeek = startOfWeek.plusDays(6); // 토요일 종료

        log.info("petId 별 주간 기록 조회: {}, startOfWeek: {}, endOfWeek: {}", petId, startOfWeek, endOfWeek);

        return getStatsWithRecordsForPeriod(petId, startOfWeek, endOfWeek);
    }

    // 특정 반려동물의 월간 산책 기록을 조회하고 통계와 상세기록 응답
    public WalkStatsWithRecordsResponse getMonthlyRecords(Long petId, LocalDate requestedDate) {
        // 요청된 날짜 없으면 오늘 날짜 사용
        if (requestedDate == null) {
            requestedDate = LocalDate.now();
        }

        // 요청된 달의 첫번째 날과 마지막 날 계산
        LocalDate startOfMonth = requestedDate.withDayOfMonth(1);
        LocalDate endOfMonth = requestedDate.with(TemporalAdjusters.lastDayOfMonth());

        log.info("월간 기록 조회: petId: {}, startOfMonth: {}, endOfMonth: {}", petId, startOfMonth, endOfMonth);

        return getStatsWithRecordsForPeriod(petId, startOfMonth, endOfMonth);
    }

    // 특정 기간 동안의 기록을 조회하고 통계와 함께 응답
    private WalkStatsWithRecordsResponse getStatsWithRecordsForPeriod(Long petId, LocalDate startDate, LocalDate endDate) {
        List<WalkRecord> records = walkRecordRepository.findAll().stream()
                .filter(record -> record.hasBuddy(petId))
                .filter(record -> isWithinRange(record.getStartDate(), startDate, endDate))
                .collect(Collectors.toList());

        log.info("Found {} records for petId: {} in the period from {} to {}", records.size(), petId, startDate, endDate);

        int count = records.size();
        double averageDistance = calculateAverageDistance(records, count);
        Duration totalDuration = calculateTotalDuration(records);
        String averageTime = calculateAverageTime(totalDuration, count);

        // 통계 정보를 담은 WalkRecordDetailResponse 생성
        WalkRecordDetailResponse stats = WalkRecordMapper.toDetailResponse(count, averageDistance, averageTime);

        // 개별 기록 리스트를 WalkRecordResponse로 변환하여 함께 반환
        List<WalkRecordResponse> recordResponses = records.stream()
                .map(WalkRecordMapper::toResponse)
                .collect(Collectors.toList());

        // 통계와 기록 리스트를 포함한 응답 생성
        return WalkStatsWithRecordsResponse.builder()
                .stats(stats)
                .records(recordResponses)
                .build();
    }

    // 평균 거리를 계산하는 메서드
    private double calculateAverageDistance(List<WalkRecord> records, int count) {
        if (count == 0) {
            return 0;
        }
        double totalDistance = records.stream().mapToDouble(WalkRecord::getDistance).sum();
        return totalDistance / count;
    }

    // 총 산책 시간을 계산하는 메서드
    private Duration calculateTotalDuration(List<WalkRecord> records) {
        return records.stream()
                .map(record -> parseDuration(record.getTotalTime()))
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }

    // "00:00:46" 형식의 문자열을 Duration 객체로 변환하는 유틸리티 메서드
    private Duration parseDuration(String timeString) {
        String[] timeParts = timeString.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    // 날짜가 특정 범위 내에 있는지 확인하는 유틸리티 메서드
    private boolean isWithinRange(LocalDate recordDate, LocalDate startDate, LocalDate endDate) {
        return (recordDate.isEqual(startDate) || recordDate.isAfter(startDate)) &&
                (recordDate.isEqual(endDate) || recordDate.isBefore(endDate));
    }

    // 평균 시간을 계산하는 유틸리티 메서드
    private String calculateAverageTime(Duration totalDuration, int count) {
        if (count == 0) {
            return "00:00:00";
        }
        long totalSeconds = totalDuration.getSeconds();
        long averageSeconds = totalSeconds / count;
        long hours = averageSeconds / 3600;
        long minutes = (averageSeconds % 3600) / 60;
        long seconds = averageSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
