package buddyguard.mybuddyguard.alert.service;

import buddyguard.mybuddyguard.alert.entity.RecordCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertEventListener {

    private final AlertService alertService;

    @EventListener
    public void handleRecordCreatedEvent(RecordCreatedEvent recordCreatedEvent) {
        alertService.sendAlertToAllPetGroup(
                recordCreatedEvent.petId(),
                recordCreatedEvent.recordType(), // 메서드에서 요구되는 것은 title != 현재 넘겨주는 것은 기록 타입
                recordCreatedEvent.message()
        );
    }
}
