package buddyguard.mybuddyguard.vaccination.controller;

import buddyguard.mybuddyguard.vaccination.controller.request.VaccinationRecordCreateRequest;
import buddyguard.mybuddyguard.vaccination.controller.request.VaccinationRecordUpdateRequest;
import buddyguard.mybuddyguard.vaccination.controller.response.VaccinationRecordResponse;
import buddyguard.mybuddyguard.vaccination.service.VaccinationRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccination")
public class VaccinationRecordController {

    private final VaccinationRecordService vaccinationRecordService;

    @Operation(summary = "예방접종 기록 생성", description = "petId 별 예방접종 기록 생성")
    @PostMapping
    public ResponseEntity<Void> createVaccinationRecord(@Valid @RequestBody VaccinationRecordCreateRequest request) {

        vaccinationRecordService.createVaccinationRecord(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "예방접종 전체 기록 조회", description = "petId 별 모든 예방접종 기록 조회")
    @GetMapping("/{petId}")
    public ResponseEntity<List<VaccinationRecordResponse>> getAllVaccinationRecords(
            @PathVariable("petId") Long petId) {
        List<VaccinationRecordResponse> records = vaccinationRecordService.getAllVaccinationRecords(
                petId);

        return ResponseEntity.ok(records);
    }

    @Operation(summary = "예방접종 기록 상세 조회", description = "petId 별 특정 예방접종 기록 조회")
    @GetMapping("/{petId}/detail/{id}")
    public ResponseEntity<VaccinationRecordResponse> getVaccinationRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id
    ) {
        VaccinationRecordResponse record = vaccinationRecordService.getVaccinationRecord(
                id, petId);

        return ResponseEntity.ok(record);
    }

    @Operation(summary = "예방접종 기록 수정", description = "petId 별 특정 예방접종 기록 수정")
    @PutMapping("/{petId}/detail/{id}")
    public ResponseEntity<Void> updateVaccinationRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id,
            @Valid @RequestBody VaccinationRecordUpdateRequest request
    ) {
        vaccinationRecordService.updateVaccinationRecord(id, petId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "예방접종 기록 삭제", description = "petId 별 특정 예방접종 기록 삭제")
    @DeleteMapping("/{petId}/detail/{id}")
    public ResponseEntity<Void> deleteVaccinationRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id
    ) {
        vaccinationRecordService.deleteVaccinationRecord(id, petId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
