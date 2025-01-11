package buddyguard.mybuddyguard.walk.controller;

import buddyguard.mybuddyguard.walk.controller.request.WalkRecordCreateRequest;
import buddyguard.mybuddyguard.walk.controller.request.WalkRecordUpdateRequest;
import buddyguard.mybuddyguard.walk.controller.response.WalkRecordResponse;
import buddyguard.mybuddyguard.walk.service.WalkRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walkRecords")
public class WalkRecordController {

    private final WalkRecordService walkRecordService;

    // 전체 산책 기록 조회
    @Operation(summary = "전체 산책 기록 조회", description = "petid 별 전체 산책 기록 조회")
    @GetMapping("/{petId}")
    public ResponseEntity<List<WalkRecordResponse>> getAllWalkRecords(
            @PathVariable("petId") Long petId) {
        List<WalkRecordResponse> records = walkRecordService.getAllWalkRecords(petId);
        return ResponseEntity.ok(records);
    }

    // 특정 산책 기록 조회
    @Operation(summary = "특정 산책 조회", description = "특정 산책 id를 이용해 특정날 산책 상세 조회")
    @GetMapping("{petId}/detail/{id}")
    public ResponseEntity<WalkRecordResponse> getWalkRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id) {
        WalkRecordResponse record = walkRecordService.getWalkRecord(id, petId);
        return ResponseEntity.ok(record);
    }

    // 산책 기록 생성
    @Operation(summary = "산책 기록 생성", description = "pet id별 산책 기록 생성")
    @PostMapping
    public ResponseEntity<Void> createWalkRecord(
            @RequestPart(name = "data", required = false) @Valid WalkRecordCreateRequest request,
            @RequestPart(name = "pathImage", required = false) MultipartFile file) {
        walkRecordService.createWalkRecord(request, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 산책 기록 업데이트
    @Operation(summary = "산책 기록 업데이트", description = "petid와 산책 기록id를 이용하여 특정 산책 기록 업데이트, startDate, endDate, endTime, totalTime, note, distance 만 수정 가능")
    @PatchMapping("{petId}/detail/{id}")
    public ResponseEntity<Void> updateWalkRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id,
            @Valid @RequestBody WalkRecordUpdateRequest request) {

        walkRecordService.updateWalkRecord(id, petId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 산책 기록 삭제
    @Operation(summary = "산책 기록 삭제", description = "petid와 산책 기록id를 이용하여 특정 산책 기록 삭제")
    @DeleteMapping("{petId}/detail/{id}")
    public ResponseEntity<Void> deleteWalkRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id) {

        walkRecordService.deleteWalkRecord(id, petId);
        return ResponseEntity.noContent().build();
    }
}
