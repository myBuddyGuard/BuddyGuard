package buddyguard.mybuddyguard.pet.mapper;

import buddyguard.mybuddyguard.pet.contoller.request.PetRegisterRequest;
import buddyguard.mybuddyguard.pet.entity.Pet;

public class PetMapper {

    public static Pet toEntity(PetRegisterRequest petRegisterRequest, String profileImage) {
        return Pet.builder()
                .name(petRegisterRequest.name())
                .profileImage(profileImage)
                .type(petRegisterRequest.type())
                .birth(petRegisterRequest.birth())
                .build();
    }
}
