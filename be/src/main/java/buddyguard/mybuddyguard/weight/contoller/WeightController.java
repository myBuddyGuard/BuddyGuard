package buddyguard.mybuddyguard.weight.contoller;

import buddyguard.mybuddyguard.weight.contoller.request.WeightCreateRequest;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import buddyguard.mybuddyguard.weight.contoller.request.WeightUpdateRequest;
import buddyguard.mybuddyguard.weight.service.WeightService;
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
@RequestMapping("/weight")
public class WeightController {

    private final WeightService weightService;

    @Operation(summary = "해당 id pet의 모든 체중 기록 조회")
    @GetMapping("/{petId}")
    public ResponseEntity<List<WeightResponse>> getAllWeightRecords(
            @PathVariable("petId") Long petId) {

        List<WeightResponse> responses = weightService.getAllWeightRecords(petId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "체중 단일 조회", description = "pet id는 체중이 해당 pet의 기록이 맞는지 검증 위해 쓰임.")
    @GetMapping("/{petId}/detail/{id}")
    public ResponseEntity<WeightResponse> getDetailWeightRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id) {

        WeightResponse response = weightService.getDetailWeightRecord(id, petId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "체중 기록 생성")
    @PostMapping
    public ResponseEntity<Void> createNewWeightRecord(@RequestBody @Valid WeightCreateRequest request) {

        weightService.createNewWeightRecord(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "체중 기록 수정", description = "pet id는 체중이 해당 pet의 기록이 맞는지 검증 위해 쓰임.")
    @PutMapping("/{petId}/detail/{id}")
    public ResponseEntity<Void> updateWeightRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id,
            @RequestBody @Valid WeightUpdateRequest request) {

        weightService.updateWeightRecord(id, petId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "체중 기록 삭제", description = "pet id는 체중이 해당 pet의 기록이 맞는지 검증 위해 쓰임.")
    @DeleteMapping("/{petId}/detail/{id}")
    public ResponseEntity<Void> deleteWeightRecord(
            @PathVariable("petId") Long petId,
            @PathVariable("id") Long id) {

        weightService.deleteWeightRecord(id, petId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
