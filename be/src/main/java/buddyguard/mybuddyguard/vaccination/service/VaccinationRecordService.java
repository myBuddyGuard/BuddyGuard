package buddyguard.mybuddyguard.vaccination.service;

import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.vaccination.controller.request.VaccinationRecordCreateRequest;
import buddyguard.mybuddyguard.vaccination.controller.request.VaccinationRecordUpdateRequest;
import buddyguard.mybuddyguard.vaccination.controller.response.VaccinationRecordResponse;
import buddyguard.mybuddyguard.vaccination.entity.VaccinationRecord;
import buddyguard.mybuddyguard.vaccination.mapper.VaccinationRecordMapper;
import buddyguard.mybuddyguard.vaccination.repository.VaccinationRecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaccinationRecordService {

    private final VaccinationRecordRepository vaccinationRecordRepository;

    @Transactional
    public void createVaccinationRecord(VaccinationRecordCreateRequest request) {
        VaccinationRecord vaccinationRecord = VaccinationRecordMapper.toEntity(request);

        VaccinationRecord savedVaccinationRecord = vaccinationRecordRepository.save(vaccinationRecord);

        log.info("SAVED VACCINATION RECORD : {}", savedVaccinationRecord);
    }

    public List<VaccinationRecordResponse> getAllVaccinationRecords(Long petId) {

        List<VaccinationRecord> vaccinationRecords = vaccinationRecordRepository.findAllByPetId(petId);

        if (vaccinationRecords.isEmpty()) {
            throw new RecordNotFoundException();
        }

        return VaccinationRecordMapper.toResponseList(vaccinationRecords);
    }

    public VaccinationRecordResponse getVaccinationRecord(Long id, Long petId) {
        VaccinationRecord vaccinationRecord = vaccinationRecordRepository.findByIdAndPetId(id, petId)
                .orElseThrow(RecordNotFoundException::new);

        return VaccinationRecordMapper.toResponse(vaccinationRecord);
    }

    @Transactional
    public void updateVaccinationRecord(Long id, Long petId, VaccinationRecordUpdateRequest request) {
        VaccinationRecord record = vaccinationRecordRepository.findByIdAndPetId(id,
                        petId)
                .orElseThrow(RecordNotFoundException::new);

        record.update(
                request.vaccinationDate(),
                request.vaccinationName(),
                request.description()
        );
    }

    @Transactional
    public void deleteVaccinationRecord(Long id, Long petId) {
        VaccinationRecord record = vaccinationRecordRepository.findByIdAndPetId(id, petId)
                .orElseThrow(RecordNotFoundException::new);

        vaccinationRecordRepository.delete(record);
    }
}
