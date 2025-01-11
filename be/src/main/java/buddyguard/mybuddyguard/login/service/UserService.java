package buddyguard.mybuddyguard.login.service;


import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.login.controller.response.UserInformationResponse;
import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPetRepository userPetRepository;

    public UserService(UserRepository userRepository, UserPetRepository userPetRepository) {
        this.userRepository = userRepository;
        this.userPetRepository = userPetRepository;
    }

    public UserInformationResponse getUserInformation(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(UserInformationNotFoundException::new);

        List<UserPet> petWithUsers = userPetRepository.findByUserId(userId);
        List<Long> petsId = new ArrayList<>();
        for (UserPet userPet : petWithUsers) {
            petsId.add(userPet.getPet().getId());
        }

        return new UserInformationResponse(userId, user.getName(), user.getEmail(),
                user.getProfileImage(), petsId);
    }
}
