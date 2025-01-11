package buddyguard.mybuddyguard.hospital.mapper;

import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordCreateRequest;
import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import java.util.List;

public class HospitalRecordMapper {

    public static HospitalRecordResponse toResponse(HospitalRecord hospitalRecord) {
        return new HospitalRecordResponse(
                hospitalRecord.getId(),
                hospitalRecord.getPetId(),
                hospitalRecord.getDate(),
                "건강",
                "병원", // 카테고리 추가
                hospitalRecord.getTitle(),
                hospitalRecord.getDescription()
        );
    }

    public static List<HospitalRecordResponse> toResponseList(List<HospitalRecord> hospitalRecords) {
        return hospitalRecords.stream()
                .map(HospitalRecordMapper::toResponse)
                .toList();
    }

    public static HospitalRecord toEntity(Long petId, HospitalRecordCreateRequest request) {
        return HospitalRecord.builder()
                .petId(petId)
                .date(request.date())
                .title(request.title())
                .description(request.description())
                .build();
    }
}
