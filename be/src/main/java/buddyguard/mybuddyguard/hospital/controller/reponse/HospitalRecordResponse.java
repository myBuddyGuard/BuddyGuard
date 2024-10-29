package buddyguard.mybuddyguard.hospital.controller.reponse;

import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import java.time.LocalDateTime;
import java.util.List;

public record HospitalRecordResponse(
        Long id,
        Long petId,
        LocalDateTime date,
        String mainCategory,   // 대카테고리
        String subCategory,    // 중카테고리
        String title,
        String description
) {
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
                .map(HospitalRecordResponse::toResponse)
                .toList();
    }

    @Override
    public String toString() {
        return "HospitalRecordResponse[" +
                "id=" + id +
                ", petId=" + petId +
                ", date=" + date +
                ", mainCategory=" + mainCategory +
                ", subCategory=" + subCategory +
                ", title=" + title +
                ", description=" + description +
                ']';
    }
}
