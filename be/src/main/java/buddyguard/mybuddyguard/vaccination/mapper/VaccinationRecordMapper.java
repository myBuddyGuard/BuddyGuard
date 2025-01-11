package buddyguard.mybuddyguard.vaccination.mapper;

import buddyguard.mybuddyguard.vaccination.controller.request.VaccinationRecordCreateRequest;
import buddyguard.mybuddyguard.vaccination.controller.response.VaccinationRecordResponse;
import buddyguard.mybuddyguard.vaccination.entity.VaccinationRecord;
import java.util.List;

public class VaccinationRecordMapper {

    public static VaccinationRecordResponse toResponse(VaccinationRecord vaccinationRecord) {
        return new VaccinationRecordResponse(
                vaccinationRecord.getId(),
                vaccinationRecord.getPetId(),
                vaccinationRecord.getVaccinationDate(),
                vaccinationRecord.getVaccinationName(),
                "건강",
                vaccinationRecord.getDescription()
        );
    }

    public static List<VaccinationRecordResponse> toResponseList(List<VaccinationRecord> vaccinationRecords) {
        return vaccinationRecords.stream()
                .map(VaccinationRecordMapper::toResponse)
                .toList();
    }

    public static VaccinationRecord toEntity(VaccinationRecordCreateRequest request) {
        return VaccinationRecord.builder()
                .petId(request.petId())
                .vaccinationDate(request.vaccinationDate())
                .vaccinationName(request.vaccinationName())
                .description(request.description())
                .build();
    }
}
