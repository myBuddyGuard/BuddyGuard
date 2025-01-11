package buddyguard.mybuddyguard.hospital.controller.reponse;

import java.time.LocalDateTime;

public record HospitalRecordResponse(
        Long id,
        Long petId,
        LocalDateTime date,
        String mainCategory,   // 대카테고리
        String subCategory,    // 중카테고리
        String title,
        String description
) {

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
