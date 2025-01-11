package buddyguard.mybuddyguard.walk.controller;

import buddyguard.mybuddyguard.walk.controller.response.WalkStatsWithRecordsResponse;
import buddyguard.mybuddyguard.walk.service.WalkRecordStatsService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walkRecords")
public class WalkRecordStatsController {

    private final WalkRecordStatsService walkRecordStatsService;

    @Operation(summary = "주간 산책 기록 조회", description = "특정 pet의 주간 산책 기록 조회")
    @GetMapping("/{petId}/weekly")
    public ResponseEntity<WalkStatsWithRecordsResponse> getWeeklyRecords(
            @PathVariable("petId") Long petId,
            @RequestParam(value = "date", required = false) LocalDate requestedDate) {

        WalkStatsWithRecordsResponse records = walkRecordStatsService.getWeeklyRecords(petId, requestedDate);
        return ResponseEntity.ok(records);
    }

    @Operation(summary = "월간 산책 기록 조회", description = "특정 pet의 월간 산책 기록 조회")
    @GetMapping("/{petId}/monthly/{month}")
    public ResponseEntity<WalkStatsWithRecordsResponse> getMonthlyRecords(
            @PathVariable("petId") Long petId,
            @PathVariable("month") int month,
            @RequestParam(value = "year", required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        LocalDate requestedDate = YearMonth.of(year, month).atDay(1);
        WalkStatsWithRecordsResponse records = walkRecordStatsService.getMonthlyRecords(petId, requestedDate);
        return ResponseEntity.ok(records);
    }
}
