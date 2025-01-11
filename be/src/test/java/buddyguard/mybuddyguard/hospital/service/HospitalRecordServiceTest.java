package buddyguard.mybuddyguard.hospital.service;

import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordCreateRequest;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordUpdateRequest;
import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.hospital.mapper.HospitalRecordMapper;
import buddyguard.mybuddyguard.hospital.repository.HospitalRecordRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class HospitalRecordServiceTest {

    @Mock
    private HospitalRecordRepository hospitalRecordRepository;

    @InjectMocks
    private HospitalRecordService hospitalRecordService;

    private HospitalRecord sampleHospitalRecord;

    @BeforeEach
    void setUp() {
        sampleHospitalRecord = new HospitalRecord(1L, 1L, LocalDateTime.now(), "병원", "정기 검진");
    }

    @Test
    void 모든_병원기록_조회_시_목록_반환() {
        // Given
        Long petId = 1L;
        List<HospitalRecord> expectedRecords = Arrays.asList(sampleHospitalRecord);
        when(hospitalRecordRepository.findByPetId(petId)).thenReturn(
                expectedRecords);

        // When
        List<HospitalRecordResponse> actualRecords = hospitalRecordService.getAllHospitalRecords(
                petId);

        // Then
        List<HospitalRecordResponse> expectedResponses = expectedRecords.stream()
                .map(record -> new HospitalRecordResponse(
                        record.getId(),
                        record.getPetId(),
                        record.getDate(),
                        "건강",
                        "병원",  // 카테고리 추가
                        record.getTitle(),
                        record.getDescription()
                )).toList();

        assertThat(actualRecords)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponses);
        verify(hospitalRecordRepository).findByPetId(petId);
    }

    @Test
    void 등록된_병원기록_조회_시_해당_목록_반환() {
        // Given
        Long hospitalRecordId = 1L;
        Long userId = 1L;
        Long petId = 1L;
        when(hospitalRecordRepository.findByIdAndPetId(hospitalRecordId,
                petId)).thenReturn(
                Optional.of(sampleHospitalRecord));

        // When
        HospitalRecordResponse result = hospitalRecordService.getHospitalRecord(hospitalRecordId,
                petId);

        // Then
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(HospitalRecordMapper.toResponse(sampleHospitalRecord));
        verify(hospitalRecordRepository).findByIdAndPetId(hospitalRecordId, petId);
    }

    @Test
    void 병원기록_생성_시_생성된_레포지토리_호출() {
        // Given
        Long hospitalRecordId = 1L;
        Long userId = 1L;
        Long petId = 1L;

        HospitalRecordCreateRequest request = new HospitalRecordCreateRequest(
                userId, petId,
                LocalDateTime.now(), "병원", "정기 검진");
        HospitalRecord hospitalRecord = HospitalRecordMapper.toEntity(
                petId, request);
        when(hospitalRecordRepository.save(any(HospitalRecord.class))).thenReturn(hospitalRecord);

        // When
        hospitalRecordService.createHospitalRecord(request);

        // Then
        verify(hospitalRecordRepository).save(any(HospitalRecord.class));
    }

    @Test
    void 존재하는_병원기록_업데이트_시_업데이트된_목록_반환() {
        // Given
        Long title = 1L;
        Long userId = 1L;
        Long petId = 1L;

        HospitalRecordUpdateRequest updateRequest = new HospitalRecordUpdateRequest(
                LocalDateTime.now(), "업데이트된 병원", "후속 진료");
        HospitalRecord updatedRecord = new HospitalRecord(title, petId,
                updateRequest.date(),
                updateRequest.title(), updateRequest.description());

        when(hospitalRecordRepository.findByIdAndPetId(title,
                petId)).thenReturn(
                Optional.of(sampleHospitalRecord));
        when(hospitalRecordRepository.save(any(HospitalRecord.class))).thenReturn(updatedRecord);

        // When
        hospitalRecordService.updateHospitalRecord(title, petId, updateRequest);

        // Then
        verify(hospitalRecordRepository).findByIdAndPetId(title,petId);
        verify(hospitalRecordRepository).save(any(HospitalRecord.class));
    }

    @Test
    void 병원기록_삭제_시_리포지토리_호출() {
        // Given
        Long hospitalRecordId = 1L;
        Long petId = 1L;
        when(hospitalRecordRepository.findByIdAndPetId(hospitalRecordId, petId))
                .thenReturn(Optional.of(sampleHospitalRecord));

        // When
        hospitalRecordService.deleteHospitalRecord(hospitalRecordId, petId);

        // Then
        verify(hospitalRecordRepository).findByIdAndPetId(hospitalRecordId,petId);
        verify(hospitalRecordRepository).delete(sampleHospitalRecord);
    }
}
