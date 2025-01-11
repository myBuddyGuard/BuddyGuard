package buddyguard.mybuddyguard.alert.service;

import buddyguard.mybuddyguard.alert.entity.RecordCreatedEvent;
import buddyguard.mybuddyguard.hospital.controller.request.HospitalRecordCreateRequest;
import buddyguard.mybuddyguard.weight.contoller.request.WeightCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RecordCreationAspect {

    private final ApplicationEventPublisher eventPublisher;

    // create* 메서드가 성공적으로 완료된 후 이벤트 발행
    @AfterReturning(value = "execution(* buddyguard.mybuddyguard.*.service.*.create*(..)) && args(request)", argNames = "request")
    public void publishRecordCreatedEvent(Object request) {
        // 여기서는 WeightCreateRequest 등 여러 기록 요청 객체가 들어올 수 있음
//        if (request instanceof WeightCreateRequest weightRequest) {
//            // petId와 관련된 이벤트를 발행
//            Long petId = weightRequest.petId(); // WeightCreateRequest에서 petId를 가져온다고 가정
//            eventPublisher.publishEvent(
//                    new RecordCreatedEvent(petId, "체중 기록", "새로운 체중 기록이 생성되었습니다."));
//            log.info("이벤트 퍼블리셔 동작 완료");
//            log.info("PET ID : {}, 기록 유형 : {}", petId, "체중 기록");
//        }
//
//        if (request instanceof HospitalRecordCreateRequest hospitalRecordCreateRequest) {
//            // petId와 관련된 이벤트를 발행
//            Long petId = hospitalRecordCreateRequest.petId(); // WeightCreateRequest에서 petId를 가져온다고 가정
//            eventPublisher.publishEvent(
//                    new RecordCreatedEvent(petId, "병원 기록", "새로운 병원 기록이 생성되었습니다."));
//            log.info("이벤트 퍼블리셔 동작 완료");
//            log.info("PET ID : {}, 기록 유형 : {}", petId, "병원 기록");
//        }
    }
}
