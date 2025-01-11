package buddyguard.mybuddyguard.pet.service;

import buddyguard.mybuddyguard.exception.UserPetGroupException;
import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import buddyguard.mybuddyguard.pet.contoller.request.PetRegisterRequest;
import buddyguard.mybuddyguard.pet.contoller.request.PetUpdateInformationRequest;
import buddyguard.mybuddyguard.pet.contoller.response.PetDetailResponse;
import buddyguard.mybuddyguard.pet.contoller.response.PetWithUserListResponse;
import buddyguard.mybuddyguard.pet.entity.Pet;
import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.exception.InvalidPetRegisterException;
import buddyguard.mybuddyguard.pet.mapper.PetMapper;
import buddyguard.mybuddyguard.pet.mapper.UserPetMapper;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import buddyguard.mybuddyguard.pet.utils.UserPetRole;
import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.walkimage.service.impl.PetProfileImageService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PetService {

    private final PetRepository repository;
    private final UserPetRepository userPetRepository;
    private final UserRepository userRepository;
    private final PetProfileImageService petProfileImageService;

    public PetService(PetRepository repository, UserPetRepository userPetRepository,
            UserRepository userRepository, PetProfileImageService petProfileImageService) {
        this.repository = repository;
        this.userPetRepository = userPetRepository;
        this.userRepository = userRepository;
        this.petProfileImageService = petProfileImageService;
    }

    /**
     * 펫을 등록한다.
     * @param petRegisterRequest
     * @param imageFile
     * @param userId
     */
    @Transactional
    public void register(PetRegisterRequest petRegisterRequest, MultipartFile imageFile,
            Long userId) {
        String imageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = petProfileImageService.uploadPetProfileImage(imageFile);
        } else {
            imageUrl = "None";
        }

        Users user = userRepository.findById(userId).orElseThrow(
                UserInformationNotFoundException::new);
        if (!validateUser(user)) {
            throw new InvalidPetRegisterException();
        }

        Pet toPet = PetMapper.toEntity(petRegisterRequest, imageUrl);
        Pet pet = repository.save(toPet);

        UserPet userPet = UserPet.builder()
                .user(user)
                .pet(pet)
                .role(UserPetRole.HOST).build();

        userPetRepository.save(userPet);

        log.info("REGISTER PET : {}번 {} 펫 등록", pet.getId(), pet.getName());
        log.info("REGISTER USER_PET : 유저 {}에게 {}번 펫 등록", user.getId(), pet.getId());
    }

    /**
     * 유저에게 등록되어 있는 모든 펫들을 조회한다.
     *
     * @param userId
     * @return
     */
    public List<PetWithUserListResponse> getPetWithUser(Long userId) {
        List<UserPet> petWithUsers = userPetRepository.findByUserId(userId);
        List<PetWithUserListResponse> response = new ArrayList<>();
        for (UserPet userPet : petWithUsers) {
            Pet pet = userPet.getPet();
            Users user = userPet.getUser();
            response.add(UserPetMapper.toPetListResponse(user, pet));
        }
        return response;
    }

    /**
     * 유저에게 등록되어 있는 단일 펫의 상세 정보를 조회한다.
     *
     * @param userId
     * @param petId
     * @return
     */
    public PetDetailResponse getOnePetWithUser(Long userId, Long petId) {
        UserPet userPetInfo = userPetRepository.findByUserIdAndPetId(userId, petId)
                .orElseThrow(UserPetGroupException::new);

        return UserPetMapper.toPetDetailResponse(userPetInfo.getUser(), userPetInfo.getPet());
    }

    /**
     * 유저에게 등록된 펫을 삭제한다. 호스트 권한의 유저가 펫을 삭제하는 경우, 게스트 유저들과 해당 펫의 연결도 모두 삭제된다.
     *
     * @param userId
     * @param petId
     */
    @Transactional
    public void delete(Long userId, Long petId) {
        UserPet userPetInfo = userPetRepository.findByUserIdAndPetId(userId, petId)
                .orElseThrow(UserPetGroupException::new);
        UserPetRole role = userPetInfo.getRole();

        userPetRepository.delete(userPetInfo);

        if (role.equals(UserPetRole.HOST)) {
            repository.deleteById(petId);
            List<UserPet> guests = userPetRepository.getAllByPetId(petId);
            if (!guests.isEmpty()) {
                userPetRepository.deleteAll(guests);
            }
        }
    }

    /**
     * 펫의 정보를 수정한다.
     *
     * @param userId
     * @param petId
     * @param petUpdateInformationRequest
     */
    @Transactional
    public void update(Long userId, Long petId,
            PetUpdateInformationRequest petUpdateInformationRequest) {
        if (!userPetRepository.existsByUserIdAndPetId(userId, petId)) {
            throw new UserPetGroupException();
        }
        Pet pet = repository.findById(petId).orElseThrow(PetNotFoundException::new);

        pet.update(petUpdateInformationRequest.name(),
                petUpdateInformationRequest.birth());

        repository.save(pet);

        log.info("UPDATE PET : {}번 펫 정보 수정 완료", petId);
    }

    /**
     * 펫의 프로필 이미지를 수정한다.
     *
     * @param userId
     * @param petId
     * @param imageFile
     */
    @Transactional
    public void updateProfileImage(Long userId, Long petId, MultipartFile imageFile) {
        String imageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = petProfileImageService.uploadPetProfileImage(imageFile);
        } else {
            imageUrl = null;
        }

        if (!userPetRepository.existsByUserIdAndPetId(userId, petId)) {
            throw new UserPetGroupException();
        }
        Pet pet = repository.findById(petId).orElseThrow(PetNotFoundException::new);

        pet.updateProfileImage(imageUrl);

        repository.save(pet);

        log.info("UPDATE PET : {}번 펫 프로필 이미지 수정 완료", petId);
    }

    /**
     * 유저에게 펫 등록이 가능한지 검증한다.
     *
     * @param user
     * @return
     */
    private boolean validateUser(Users user) {
        return !userPetRepository.existsByUserExceedPetCount(user.getId());
    }
}
