package buddyguard.mybuddyguard.hospital.service;

import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordCreateRequest;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordUpdateRequest;
import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.hospital.mapper.HospitalRecordMapper;
import buddyguard.mybuddyguard.hospital.repository.HospitalRecordRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalRecordService {

    private final HospitalRecordRepository hospitalRecordRepository;

    public List<HospitalRecordResponse> getAllHospitalRecords(Long petId) {
        List<HospitalRecord> records = hospitalRecordRepository.findByPetId(petId);

        if (records.isEmpty()) {
            throw new RecordNotFoundException();
        }

        return HospitalRecordMapper.toResponseList(records);
    }

    public HospitalRecordResponse getHospitalRecord(Long id, Long petId) {
        HospitalRecord record = hospitalRecordRepository.findByIdAndPetId(id, petId).orElseThrow(RecordNotFoundException::new);
        return HospitalRecordMapper.toResponse(record);
    }

    @Transactional
    public void createHospitalRecord(HospitalRecordCreateRequest request) {
        HospitalRecord hospitalRecord = HospitalRecordMapper.toEntity(
                request.petId(), request);

        HospitalRecord saveHospitalRecord = hospitalRecordRepository.save(hospitalRecord);

        log.info("SAVED HOSPITAL RECORD : {}", saveHospitalRecord);
    }

    @Transactional
    public void updateHospitalRecord(Long id, Long petId,
            HospitalRecordUpdateRequest request) {
        HospitalRecord hospitalRecord = hospitalRecordRepository.findByIdAndPetId(id,
                        petId)
                .orElseThrow(RecordNotFoundException::new); // 예외 처리

        hospitalRecord.update(
                request.description(),
                request.title(),
                request.date()
        );
        hospitalRecordRepository.save(hospitalRecord);
    }

    @Transactional
    public void deleteHospitalRecord(Long id, Long petId) {
        HospitalRecord record = hospitalRecordRepository.findByIdAndPetId(id, petId)
                .orElseThrow(RecordNotFoundException::new); // 예외 처리

        hospitalRecordRepository.delete(record);
    }
}
