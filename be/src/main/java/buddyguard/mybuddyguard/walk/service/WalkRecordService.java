package buddyguard.mybuddyguard.walk.service;

import buddyguard.mybuddyguard.exception.ImageFileRequiredException;
import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.pet.entity.Pet;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.walk.controller.response.WalkRecordResponse;
import buddyguard.mybuddyguard.walk.controller.request.WalkRecordCreateRequest;
import buddyguard.mybuddyguard.walk.controller.request.WalkRecordUpdateRequest;
import buddyguard.mybuddyguard.walk.entity.PetWalkRecord;
import buddyguard.mybuddyguard.walk.entity.WalkRecord;
import buddyguard.mybuddyguard.walk.entity.WalkRecordCenterPosition;
import buddyguard.mybuddyguard.walk.entity.WalkRecordPath;
import buddyguard.mybuddyguard.walk.mapper.WalkRecordMapper;
import buddyguard.mybuddyguard.walk.repository.PetWalkRecordRepository;
import buddyguard.mybuddyguard.walk.repository.WalkRecordCenterPositionRepository;
import buddyguard.mybuddyguard.walk.repository.WalkRecordPathRepository;
import buddyguard.mybuddyguard.walk.repository.WalkRecordRepository;
import buddyguard.mybuddyguard.walkimage.entity.WalkS3Image;
import buddyguard.mybuddyguard.walkimage.service.WalkImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkRecordService {

    private final WalkRecordRepository walkRecordRepository;
    private final WalkRecordCenterPositionRepository walkRecordCenterPositionRepository;
    private final WalkImageService walkImageService;
    private final PetRepository petRepository;
    private final WalkRecordPathRepository walkRecordPathRepository;
    private final PetWalkRecordRepository petWalkRecordRepository;

    // 특정 반려동물의 전체 산책 기록 조회
    public List<WalkRecordResponse> getAllWalkRecords(Long petId) {
        return walkRecordRepository.findAll().stream()
                .filter(record -> record.hasBuddy(petId)) // buddyIds에서 해당 petId를 확인
                .map(WalkRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 특정 산책 기록에서 특정 반려동물이 참여한 기록만 조회
    public WalkRecordResponse getWalkRecord(Long id, Long petId) {
        return walkRecordRepository.findById(id)
                .filter(record -> record.hasBuddy(petId)) // 특정 기록에서 petId 확인
                .map(WalkRecordMapper::toResponse)
                .orElseThrow(RecordNotFoundException::new); // petId가 포함된 기록이 없을 때 예외 처리
    }

    @Transactional
    public void createWalkRecord(WalkRecordCreateRequest request, MultipartFile file) {
        WalkS3Image walkS3Image;

        // 이미지 파일 존재하면 S3에 업로드 후 WalkS3Image 엔티티 생성
        if (file != null && !file.isEmpty()) {
            walkS3Image = walkImageService.uploadWalkImage(file);
        } else {
            throw new ImageFileRequiredException();
        }

        // WalkRecordCenterPosition 생성 및 저장
        WalkRecordCenterPosition centerPosition = request.centerPosition().toWalkRecordCenterPosition();
        walkRecordCenterPositionRepository.save(centerPosition);  // CenterPosition 저장

        // WalkRecord 엔티티 생성 (필요한 값들로만 생성)
        WalkRecord walkRecord = request.toWalkRecord(walkS3Image,centerPosition);

        // 먼저 WalkRecord 저장
        WalkRecord savedWalkRecord = walkRecordRepository.save(walkRecord);

        // PetWalkRecord 생성
        List<PetWalkRecord> petWalkRecords = request.buddysId().stream()
                .map(petId -> {
                    Pet pet = petRepository.findById(petId)
                            .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
                    return PetWalkRecord.builder()
                            .pet(pet)
                            .walkRecord(savedWalkRecord)
                            .build();
                })
                .toList();

        // WalkRecordPath 생성
        List<WalkRecordPath> walkRecordPaths = request.path().stream()
                .map(pathRequest ->
                        WalkRecordPath.builder()
                                .latitude(pathRequest.getLatitude())
                                .longitude(pathRequest.getLongitude())
                                .walkRecord(savedWalkRecord)
                        .build())
                .toList();

        // 연관된 엔티티 추가 후 업데이트
        savedWalkRecord.getPetWalkRecords().addAll(petWalkRecords);  // PetWalkRecord 리스트 추가
        savedWalkRecord.getPath().addAll(walkRecordPaths);  // WalkRecordPath 리스트 추가

        // WalkRecord 업데이트
        walkRecordRepository.save(savedWalkRecord);
        petWalkRecordRepository.saveAll(petWalkRecords);
        walkRecordPathRepository.saveAll(walkRecordPaths);

        log.info("SAVED WALK RECORD: {}", savedWalkRecord);
    }

    @Transactional
    public void updateWalkRecord(Long id, Long petId, WalkRecordUpdateRequest request) {
        WalkRecord walkRecord = walkRecordRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new); // 예외 처리

        if (!walkRecord.hasBuddy(petId)) {
            throw new RecordNotFoundException(); // petId가 기록에 없을 때 예외 처리
        }

        // 요청 데이터를 이용해 필요한 필드만 업데이트 (applyUpdates 호출)
        request.updateWalkRecord(walkRecord);  // 필요한 필드만 업데이트

        walkRecordRepository.save(walkRecord);
    }

    @Transactional
    public void deleteWalkRecord(Long id, Long petId) {
        WalkRecord walkRecord = walkRecordRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new); // 예외 처리

        // 해당 기록에 petId가 포함되어 있는지 확인 후 삭제
        if (walkRecord.hasBuddy(petId)) {
            walkRecordRepository.delete(walkRecord);
        } else {
            throw new RecordNotFoundException(); // petId가 기록에 없을 때 예외 처리
        }
    }
}
