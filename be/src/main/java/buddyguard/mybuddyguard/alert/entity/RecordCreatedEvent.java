package buddyguard.mybuddyguard.alert.entity;

public record RecordCreatedEvent(Long petId, String recordType, String message) {
}
