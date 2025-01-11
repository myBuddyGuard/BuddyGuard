package buddyguard.mybuddyguard.schedule.repository;

import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.hospital.entity.QHospitalRecord;
import buddyguard.mybuddyguard.weight.entity.QWeight;
import buddyguard.mybuddyguard.weight.entity.Weight;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ScheduleRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Weight> findWeightRecordAllByYearAndMonth(Long petId, int year, int month) {
        QWeight weightRecord = QWeight.weight1;

        return jpaQueryFactory.selectFrom(weightRecord)
                .where(weightRecord.recordedAt.year().eq(year)
                        .and(weightRecord.recordedAt.month().eq(month)))
                .fetch();
    }

    @Override
    public List<HospitalRecord> findHospitalRecordAllByYearAndMonth(Long petId, int year, int month) {
        QHospitalRecord hospitalRecord = QHospitalRecord.hospitalRecord;

        return jpaQueryFactory.selectFrom(hospitalRecord)
                .where(hospitalRecord.date.year().eq(year)
                        .and(hospitalRecord.date.month().eq(month)))
                .fetch();
    }
}
