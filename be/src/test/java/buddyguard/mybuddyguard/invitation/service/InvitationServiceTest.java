package buddyguard.mybuddyguard.invitation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.invitation.controller.response.InvitationLinkResponse;
import buddyguard.mybuddyguard.invitation.entity.InvitationInformation;
import buddyguard.mybuddyguard.invitation.exception.InvalidInvitationException;
import buddyguard.mybuddyguard.invitation.exception.InvitationLinkExpiredException;
import buddyguard.mybuddyguard.invitation.exception.UserPetGroupNotFound;
import buddyguard.mybuddyguard.invitation.repository.InvitationRepository;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.login.entity.Users;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import buddyguard.mybuddyguard.pet.entity.Pet;
import buddyguard.mybuddyguard.pet.entity.UserPet;
import buddyguard.mybuddyguard.pet.exception.InvalidPetRegisterException;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import buddyguard.mybuddyguard.pet.utils.UserPetRole;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataRedisTest
@ExtendWith(SpringExtension.class)
public class InvitationServiceTest {

    private InvitationService invitationService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserPetRepository userPetRepository;
    private final String uuid = "69988103-4bea-4def-8391-ecc8ac9804da";
    private Users user;
    private Pet pet;

    @BeforeEach
    public void setup() {
        // 직접 객체를 주입하여 service 생성
        MockitoAnnotations.openMocks(this);
        invitationService = new InvitationService(userRepository, petRepository, userPetRepository,
                invitationRepository);

        user = new Users();
        user.setId(1L);
        user.setName("tester");

        pet = Pet.builder().id(1L).build();
    }

    @AfterEach
    void finish() {
        invitationRepository.deleteById(uuid);
    }

    @Test
    void 정상적으로_링크_생성() {
        // GIVEN
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(true);
        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(true);

        // WHEN
        InvitationLinkResponse invitationLink = invitationService.makeInvitationLink(user.getId(),
                pet.getId());

        String[] linkParts = invitationLink.invitationlink().split("/");
        String uuid = linkParts[linkParts.length - 1];

        // THEN
        assertThat(invitationLink).isNotNull();
        InvitationInformation savedInvitation = invitationRepository.findById(uuid).orElseThrow();

        assertThat(savedInvitation.getUserId()).isEqualTo(user.getId());
        assertThat(savedInvitation.getPetId()).isEqualTo(pet.getId());
    }

    @Test
    void 링크_생성_시_펫이_없는_경우_예외_발생() {
        // GIVEN
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(true);
        when(petRepository.existsById(pet.getId())).thenThrow(PetNotFoundException.class);
        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(false);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.makeInvitationLink(user.getId(), pet.getId()))
                .isInstanceOf(PetNotFoundException.class);
    }

    @Test
    void 링크_생성_시_유저가_없는_경우_예외_발생() {
        // GIVEN
        when(userRepository.existsById(user.getId())).thenThrow(
                UserInformationNotFoundException.class);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(true);
        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(false);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.makeInvitationLink(user.getId(), pet.getId()))
                .isInstanceOf(UserInformationNotFoundException.class);
    }

    @Test
    void 링크_생성_시_펫_유저_관계가_없는_경우_예외_발생() {
        // GIVEN
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(true);
        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(false);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.makeInvitationLink(user.getId(), pet.getId()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void 링크_생성_시_호스트가_아닌_경우_예외_발생() {
        // GIVEN
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(false);
        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(userPetRepository.existsByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(false);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.makeInvitationLink(user.getId(), pet.getId()))
                .isInstanceOf(InvalidInvitationException.class);
    }

    @Test
    void 초대_링크로_정상_그룹_가입() {
        // GIVEN
        UserPet userPet = UserPet.builder()
                .user(user)
                .pet(pet)
                .role(UserPetRole.GUEST).build();

        InvitationInformation invitation = InvitationInformation.builder().userId(user.getId())
                .petId(pet.getId()).id(uuid).build();

        invitationRepository.save(invitation);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userPetRepository.save(any(UserPet.class))).thenReturn(userPet);
        when(userPetRepository.existsByUserExceedPetCount(user.getId())).thenReturn(false);
        when(userPetRepository.existsByPetIdAndRole(pet.getId(), UserPetRole.HOST)).thenReturn(
                true);
        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(
                Optional.empty());

        // WHEN
        invitationService.register(uuid, user.getId());

        // THEN
        verify(userPetRepository).save(any(UserPet.class));
    }

    @Test
    void 유저가_존재하지_않는_경우_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                UserInformationNotFoundException.class);
    }

    @Test
    void 펫이_존재하지_않는_경우_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        when(petRepository.findById(pet.getId())).thenReturn(Optional.empty());

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                UserInformationNotFoundException.class);
    }

    @Test
    void 링크가_만료된_경우_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                InvitationLinkExpiredException.class);
    }

    @Test
    void 펫_3마리_이상_유저가_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        InvitationInformation invitation = InvitationInformation.builder().userId(user.getId())
                .petId(pet.getId()).id(uuid).build();

        invitationRepository.save(invitation);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userPetRepository.existsByUserExceedPetCount(user.getId())).thenReturn(true);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                InvalidPetRegisterException.class);
    }

    @Test
    void 호스트가_존재하지_않는_그룹에_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        InvitationInformation invitation = InvitationInformation.builder().userId(user.getId())
                .petId(pet.getId()).id(uuid).build();

        invitationRepository.save(invitation);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userPetRepository.existsByUserExceedPetCount(user.getId())).thenReturn(false);
        when(userPetRepository.existsByUserIdAndPetIdAndRole(user.getId(), pet.getId(),
                UserPetRole.HOST)).thenReturn(true);
        when(userPetRepository.existsByPetIdAndRole(pet.getId(), UserPetRole.HOST)).thenReturn(
                false);

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                UserPetGroupNotFound.class);
    }

    @Test
    void 이미_가입된_그룹에_초대_링크로_그룹_가입_시_예외_발생() {
        // GIVEN
        InvitationInformation invitation = InvitationInformation.builder().userId(user.getId())
                .petId(pet.getId()).id(uuid).build();

        UserPet userPet = UserPet.builder()
                .user(user)
                .pet(pet)
                .role(UserPetRole.GUEST).build();

        invitationRepository.save(invitation);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(userPetRepository.existsByUserExceedPetCount(user.getId())).thenReturn(false);
        when(userPetRepository.existsByPetIdAndRole(pet.getId(), UserPetRole.HOST)).thenReturn(
                true);
        when(userPetRepository.findByUserIdAndPetId(user.getId(), pet.getId())).thenReturn(
                Optional.of(userPet));

        // WHEN, THEN
        assertThatThrownBy(() -> invitationService.register(uuid, user.getId())).isInstanceOf(
                InvalidPetRegisterException.class);
    }
}
