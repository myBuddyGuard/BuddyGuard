package buddyguard.mybuddyguard.schedule.controller.response;

import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import java.util.List;

public record ScheduleMonthlyResponse(
        List<WeightResponse> weightRecords,
        List<HospitalRecordResponse> hospitalRecords
) {

}
