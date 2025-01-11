package buddyguard.mybuddyguard.weight.service;

import buddyguard.mybuddyguard.alert.service.AlertService;
import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.weight.entity.Weight;
import buddyguard.mybuddyguard.weight.mapper.WeightMapper;
import buddyguard.mybuddyguard.weight.contoller.request.WeightCreateRequest;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import buddyguard.mybuddyguard.weight.contoller.request.WeightUpdateRequest;
import buddyguard.mybuddyguard.weight.repository.WeightRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeightService {

    private final WeightRepository weightRepository;
    private final AlertService alertService;

    @Transactional
    public void createNewWeightRecord(WeightCreateRequest request) {
        Weight weight = WeightMapper.toEntity(request);

        Weight savedWeight = weightRepository.save(weight);

        log.info("SAVED WEIGHT : {}", savedWeight);
    }

    public List<WeightResponse> getAllWeightRecords(Long petId) {

        List<Weight> weights = weightRepository.findAllByPetId(petId);

        if (weights.isEmpty()) {
            throw new RecordNotFoundException();
        }

        return WeightMapper.toResponseList(weights);
    }

    public WeightResponse getDetailWeightRecord(Long id, Long petId) {

        Weight weight = weightRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        weight.validateOwnership(petId);

        return WeightMapper.toResponse(weight);
    }

    @Transactional
    public void updateWeightRecord(Long id, Long petId, WeightUpdateRequest request) {

        Weight weight = weightRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        weight.validateOwnership(petId);

        weight.update( // 변경 포인트를 해당 메서드에 모아서 하나로 통일한다.
                request.recordedAt(),
                request.weight(),
                request.description()
        );
        // dirty checking 으로 자동 변경 되고 db에 저장 된다.
    }

    @Transactional
    public void deleteWeightRecord(Long id, Long petId) {

        Weight weight = weightRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        weight.validateOwnership(petId);

        weightRepository.delete(weight);
    }
}
