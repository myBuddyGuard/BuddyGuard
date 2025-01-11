package buddyguard.mybuddyguard.pet.contoller.response;

public record PetWithUserListResponse(
        Long userId,
        Long petId,
        String petName,
        String profileImage
) {

}
