package buddyguard.mybuddyguard.schedule.controller;

import buddyguard.mybuddyguard.schedule.controller.response.ScheduleMonthlyResponse;
import buddyguard.mybuddyguard.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "펫의 달별 기록을 가져오는 api", description = "특정 펫의 특정 달의 기록을 가져옵니다. 해당 펫이 존재하지 않거나, 펫 그룹에 로그인 유저가 속하지 않을 경우 예외가 발생합니다.")
    @GetMapping("/schedule/{petId}/{year}/{month}")
    public ResponseEntity<ScheduleMonthlyResponse> getMonthlySchedule(
            @PathVariable("petId") Long petId,
            @PathVariable("year") int year, @PathVariable("month") int month,
            HttpServletRequest request) {
        ScheduleMonthlyResponse response = scheduleService.getMonthly(petId, year, month, request);
        return ResponseEntity.ok(response);
    }
}
