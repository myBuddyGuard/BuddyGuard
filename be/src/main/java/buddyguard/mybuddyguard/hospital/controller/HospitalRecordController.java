package buddyguard.mybuddyguard.hospital.controller;

import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordCreateRequest;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordUpdateRequest;
import buddyguard.mybuddyguard.hospital.service.HospitalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospitalRecords/{petId}")
public class HospitalRecordController {

    private final HospitalRecordService hospitalRecordService;

    @Operation(summary = "병원 전체 기록 조회", description = "petId 별 모든 병원 기록 조회")
    @GetMapping
    public ResponseEntity<List<HospitalRecordResponse>> getAllHospitalRecords(
            @PathVariable("petId") Long petId) {
        List<HospitalRecordResponse> records = hospitalRecordService.getAllHospitalRecords(
                petId);
        return ResponseEntity.ok(records);
    }

    @Operation(summary = "병원 기록 상세 조회", description = "petId 별 특정 병원 기록 조회")
    @GetMapping("/detail/{id}")
    public ResponseEntity<HospitalRecordResponse> getHospitalRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id) {
        HospitalRecordResponse record = hospitalRecordService.getHospitalRecord(id, petId);
        return ResponseEntity.ok(record);
    }

    @Operation(summary = "병원 기록 생성", description = "petId 별 병원 기록 생성")
    @PostMapping
    public ResponseEntity<Void> createHospitalRecord(
            @Valid @RequestBody HospitalRecordCreateRequest request) {
        hospitalRecordService.createHospitalRecord(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "병원 기록 수정", description = "petId 별 특정 병원 기록 수정")
    @PutMapping("/detail/{id}")
    public ResponseEntity<Void> updateHospitalRecord(
            @PathVariable Long petId,
            @PathVariable Long id,
            @Valid @RequestBody HospitalRecordUpdateRequest request) {

        hospitalRecordService.updateHospitalRecord(petId, id, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "병원 기록 삭제", description = "petId 별 특정 병원 기록 삭제")
    @DeleteMapping("/detail/{id}")
    public ResponseEntity<Void> deleteHospitalRecord(
            @PathVariable Long id,
            @PathVariable Long petId
            ) {

        hospitalRecordService.deleteHospitalRecord(id,petId);

        return ResponseEntity.noContent().build();
    }
}
