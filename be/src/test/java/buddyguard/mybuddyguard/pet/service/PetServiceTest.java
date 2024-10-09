package buddyguard.mybuddyguard.pet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import buddyguard.mybuddyguard.pet.contoller.request.PetRegisterRequest;
import buddyguard.mybuddyguard.pet.contoller.request.PetUpdateInformationRequest;
import buddyguard.mybuddyguard.pet.contoller.response.PetWithUserListResponse;
import buddyguard.mybuddyguard.pet.entity.Pet;
import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.exception.InvalidPetRegisterException;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import buddyguard.mybuddyguard.pet.utils.PetType;
import buddyguard.mybuddyguard.pet.utils.UserPetRole;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserPetRepository userPetRepository;

    @InjectMocks
    private PetService petService;

    private Users user;

    private Pet pet;

    @BeforeEach
    void 유저와_펫_생성() {
        user = new Users();
        user.setId(1L);
        user.setName("tester");

        pet = Pet.builder().id(1L).build();
    }

    @Test
    void 정상적으로_펫_등록() {
        // GIVEN
        PetRegisterRequest petRegisterRequest = new PetRegisterRequest(user.getId(), "toto",
                "www.naver.com", PetType.DOG, LocalDate.now());

        Pet insertPet = Pet.builder().id(1L).birth(petRegisterRequest.birth())
                .type(petRegisterRequest.type()).profileImage(petRegisterRequest.profileImage())
                .name(petRegisterRequest.name()).build();

        UserPet userPet = UserPet.builder().id(1L).user(user).pet(insertPet).role(
                UserPetRole.HOST).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(petRepository.save(any(Pet.class))).thenReturn(insertPet);
        when(userPetRepository.save(any(UserPet.class))).thenReturn(userPet);

        // WHEN
        petService.register(petRegisterRequest);

        // THEN
        verify(petRepository).save(any(Pet.class));
        verify(userPetRepository).save(any(UserPet.class));
    }

    @Test
    void 펫_3마리_이상_유저_펫_등록시_예외_발생() {
        // GIVEN
        PetRegisterRequest petRegisterRequest = new PetRegisterRequest(user.getId(), "toto",
                "www.naver.com", PetType.DOG, LocalDate.now());

        Pet insertPet = Pet.builder().id(1L).birth(petRegisterRequest.birth())
                .type(petRegisterRequest.type()).profileImage(petRegisterRequest.profileImage())
                .name(petRegisterRequest.name()).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userPetRepository.existsByUserExceedPetCount(user.getId())).thenReturn(true);

        // WHEN, THEN
        assertThatThrownBy(() -> petService.register(petRegisterRequest)).isInstanceOf(
                InvalidPetRegisterException.class);

    }

    @Test
    void 유저가_존재하지_않는_경우_펫_등록시_예외_발생() {
        // GIVEN
        PetRegisterRequest petRegisterRequest = new PetRegisterRequest(user.getId(), "toto",
                "www.naver.com", PetType.DOG, LocalDate.now());

        Pet insertPet = Pet.builder().id(1L).birth(petRegisterRequest.birth())
                .type(petRegisterRequest.type()).profileImage(petRegisterRequest.profileImage())
                .name(petRegisterRequest.name()).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> petService.register(petRegisterRequest)).isInstanceOf(
                UserInformationNotFoundException.class);

    }

    @Test
    void 특정_유저의_펫_전체리스트_정상_조회() {
        // GIVEN
        Pet pet2 = Pet.builder().id(2L).build();

        UserPet userPet = UserPet.builder().id(1L).user(user).pet(pet).role(
                UserPetRole.HOST).build();
        UserPet userPet2 = UserPet.builder().id(2L).user(user).pet(pet2).role(
                UserPetRole.HOST).build();
        List<UserPet> list = new ArrayList<>();
        list.add(userPet);
        list.add(userPet2);

        when(userPetRepository.findByUserId(user.getId())).thenReturn(list);

        // WHEN
        List<PetWithUserListResponse> result = petService.getPetWithUser(user.getId());

        // THEN
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).petId()).isEqualTo(1L);
        assertThat(result.get(0).userId()).isEqualTo(1L);

        assertThat(result.get(1).petId()).isEqualTo(2L);
        assertThat(result.get(1).userId()).isEqualTo(1L);
    }

    @Test
    void 특정_유저의_펫_전체리스트_조회시_정보가_존재하지_않는_경우() {
        // GIVEN
        List<UserPet> list = new ArrayList<>();

        when(userPetRepository.findByUserId(user.getId())).thenReturn(list);

        // WHEN
        List<PetWithUserListResponse> result = petService.getPetWithUser(user.getId());

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    void 특정_유저의_단일_펫_정상_조회() {
        // GIVEN
        Pet pet2 = Pet.builder().id(2L).build();

        UserPet userPet = UserPet.builder().id(1L).user(user).pet(pet).role(
                UserPetRole.HOST).build();
        UserPet userPet2 = UserPet.builder().id(2L).user(user).pet(pet2).role(
                UserPetRole.HOST).build();
        List<UserPet> list = new ArrayList<>();
        list.add(userPet);
        list.add(userPet2);

        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet2.getId())).thenReturn(
                Optional.of(userPet2));

        // WHEN
        PetWithUserListResponse result = petService.getOnePetWithUser(user.getId(), pet2.getId());

        // THEN
        assertThat(result.petId()).isEqualTo(2L);
    }

    @Test
    void 특정_유저의_단일_펫_조회시_정보가_존재하지_않는_경우() {
        // GIVEN
        when(userPetRepository.findByUserIdAndPetId(user.getId(), 3L)).thenReturn(Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> petService.getOnePetWithUser(user.getId(), 3L))
                .isInstanceOf(UserInformationNotFoundException.class);
    }

    @Test
    void 호스트_유저의_펫_정상_삭제() {
        // GIVEN
        Users user2 = new Users();
        user2.setId(2L);

        List<UserPet> guests = new ArrayList<>();
        UserPet userPet = UserPet.builder().id(1L).user(user).pet(pet).role(
                UserPetRole.HOST).build();
        UserPet userPet2 = UserPet.builder().id(2L).user(user2).pet(pet).role(
                UserPetRole.GUEST).build();
        guests.add(userPet2);

        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(
                Optional.of(userPet));
        when(userPetRepository.getAllByPetId(pet.getId())).thenReturn(guests);

        // WHEN
        petService.delete(user.getId(), pet.getId());

        // THEN
        verify(userPetRepository).delete(userPet);
        verify(userPetRepository).findByUserIdAndPetId(user.getId(), pet.getId());
        verify(userPetRepository).deleteAll(guests);
    }

    @Test
    void 게스트_유저의_펫_정상_삭제() {
        // GIVEN
        Users guest = new Users();
        guest.setId(2L);

        UserPet hostInfo = UserPet.builder().id(1L).user(user).pet(pet).role(
                UserPetRole.HOST).build();
        UserPet guestInfo = UserPet.builder().id(2L).user(guest).pet(pet).role(
                UserPetRole.GUEST).build();

        when(userPetRepository.findByUserIdAndPetId(guest.getId(), pet.getId())).thenReturn(
                Optional.of(guestInfo));
        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(
                Optional.of(hostInfo));

        // WHEN
        petService.delete(guest.getId(), pet.getId());

        // THEN
        verify(userPetRepository).delete(guestInfo);
        Optional<UserPet> hostResult = userPetRepository.findByUserIdAndPetId(user.getId(),
                pet.getId());
        assertThat(hostResult).isPresent();
        assertThat(hostResult.get().getRole()).isEqualTo(UserPetRole.HOST);
    }

    @Test
    void 정보가_없는_경우_펫_삭제시_예외_발생() {
        // GIVEN
        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(
                Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> petService.delete(user.getId(), pet.getId())).isInstanceOf(
                UserInformationNotFoundException.class);

    }

    @Test
    void 펫_정보_정상_수정() {
        // GIVEN
        PetUpdateInformationRequest updateInformationRequest = new PetUpdateInformationRequest(
                "new name", "www.example.com", LocalDate.now());

        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(true);
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        doAnswer(invocation -> {
            Pet afterPet = invocation.getArgument(0);
            afterPet.update(updateInformationRequest.name(),
                    updateInformationRequest.profileImage(), null);
            return afterPet;
        }).when(petRepository).save(pet);

        // WHEN
        petService.update(user.getId(), pet.getId(), updateInformationRequest);

        // THEN
        verify(petRepository).save(pet);
        assertThat(pet.getName()).isEqualTo(updateInformationRequest.name());
        assertThat(pet.getProfileImage()).isEqualTo(updateInformationRequest.profileImage());

    }

    @Test
    void 정보가_없는_경우_펫_정보_수정_시_예외_발생() {
        // GIVEN
        PetUpdateInformationRequest updateInformationRequest = new PetUpdateInformationRequest(
                "new name", "www.example.com", LocalDate.now());

        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenThrow(
                UserInformationNotFoundException.class);

        // WHEN, THEN
        assertThatThrownBy(() -> petService.update(user.getId(), pet.getId(),
                updateInformationRequest)).isInstanceOf(UserInformationNotFoundException.class);

    }

    @Test
    void 펫_정보가_없는_경우_펫_정보_수정_시_예외_발생() {
        // GIVEN
        PetUpdateInformationRequest updateInformationRequest = new PetUpdateInformationRequest(
                "new name", "www.example.com", LocalDate.now());

        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(true);
        when(petRepository.findById(pet.getId())).thenReturn(Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> petService.update(user.getId(), pet.getId(),
                updateInformationRequest)).isInstanceOf(PetNotFoundException.class);

    }
}
