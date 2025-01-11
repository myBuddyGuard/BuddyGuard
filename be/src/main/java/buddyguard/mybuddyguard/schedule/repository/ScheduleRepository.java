package buddyguard.mybuddyguard.schedule.repository;

import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.schedule.controller.response.ScheduleMonthlyResponse;
import buddyguard.mybuddyguard.weight.entity.Weight;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository {
    List<Weight> findWeightRecordAllByYearAndMonth(Long petId, int year, int month);

    List<HospitalRecord> findHospitalRecordAllByYearAndMonth(Long petId, int year, int month);

}
