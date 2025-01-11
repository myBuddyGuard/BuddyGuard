package buddyguard.mybuddyguard.feed.service;

import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.feed.aop.UserPetGroupValidation;
import buddyguard.mybuddyguard.feed.controller.request.FeedRecordCreateRequest;
import buddyguard.mybuddyguard.feed.controller.request.FeedRecordUpdateRequest;
import buddyguard.mybuddyguard.feed.controller.response.FeedRecordResponse;
import buddyguard.mybuddyguard.feed.entity.FeedRecord;
import buddyguard.mybuddyguard.feed.repository.FeedRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    @UserPetGroupValidation
    public List<FeedRecordResponse> getPetFeedRecord(Long petId, Long userId) {

        List<FeedRecord> feedRecords = feedRepository.findByPetId(petId);

        List<FeedRecordResponse> response = new ArrayList<>();
        for (FeedRecord feedRecord : feedRecords) {
            response.add(feedRecord.toDto());
        }

        return response;
    }

    @Transactional
    @UserPetGroupValidation
    public void save(Long petId, Long userId, FeedRecordCreateRequest feedRecordCreateRequest) {
        FeedRecord feedRecord = feedRecordCreateRequest.toEntity(petId);

        feedRepository.save(feedRecord);

        log.info("SAVE FEED RECORD : {}번 펫 먹이 기록 등록", feedRecord.getPetId());
    }

    @Transactional
    @UserPetGroupValidation
    public void delete(Long petId, Long userId, Long feedId) {
        FeedRecord feedRecord = feedRepository.findById(feedId)
                .orElseThrow(RecordNotFoundException::new);

        feedRepository.delete(feedRecord);

        log.info("DELETE FEED RECORD : {}번 펫 먹이 기록 삭제", feedRecord.getPetId());
    }

    @Transactional
    @UserPetGroupValidation
    public void update(Long petId, Long userId, Long feedId, FeedRecordUpdateRequest feedRecordUpdateRequest) {
        FeedRecord feedRecord = feedRepository.findById(feedId)
                .orElseThrow(RecordNotFoundException::new);

        feedRecord.update(feedRecordUpdateRequest.amount(), feedRecordUpdateRequest.amountType(),
                feedRecordUpdateRequest.feedType(), feedRecordUpdateRequest.date());

        feedRepository.save(feedRecord);

        log.info("UPDATE FEED RECORE : {}번 펫의 {}번 기록 수정 완료", petId, feedRecord.getId());
    }
}
