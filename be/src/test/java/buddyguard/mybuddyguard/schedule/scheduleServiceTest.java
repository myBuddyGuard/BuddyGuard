package buddyguard.mybuddyguard.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.pet.entity.Pet;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import buddyguard.mybuddyguard.schedule.controller.response.ScheduleMonthlyResponse;
import buddyguard.mybuddyguard.schedule.repository.ScheduleRepository;
import buddyguard.mybuddyguard.schedule.service.ScheduleService;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import buddyguard.mybuddyguard.weight.entity.Weight;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class scheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserPetRepository userPetRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void 펫_아이디로_기록이_있는_달_정상_검색() {
        // GIVEN
        Pet pet = Pet.builder().id(1L).build();
        String token = "token";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        List<Weight> weights = List.of(
                new Weight(pet.getId(), LocalDateTime.of(2024, 10, 1, 0, 0), 3.3, "정상"));

        List<HospitalRecord> hospitals = List.of(
                HospitalRecord.builder().petId(pet.getId()).title("병원 방문1")
                        .date(LocalDateTime.of(2024, 10, 30, 0, 0)).build(),
                HospitalRecord.builder().petId(pet.getId()).title("병원 방문2")
                        .date(LocalDateTime.of(2024, 10, 9, 0, 0)).build());

        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(tokenService.getUserId(token)).thenReturn(1L);
        when(userPetRepository.existsByUserIdAndPetId(tokenService.getUserId(token),
                pet.getId())).thenReturn(true);
        when(scheduleRepository.findHospitalRecordAllByYearAndMonth(pet.getId(), 2024,
                10)).thenReturn(hospitals);
        when(scheduleRepository.findWeightRecordAllByYearAndMonth(pet.getId(), 2024,
                10)).thenReturn(weights);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // WHEN
        ScheduleMonthlyResponse result = scheduleService.getMonthly(pet.getId(), 2024, 10, request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.hospitalRecords().size()).isEqualTo(2);
        assertThat(result.weightRecords().size()).isEqualTo(1);
        assertThat(result.weightRecords().get(0)).isInstanceOf(WeightResponse.class);
        assertThat(result.hospitalRecords().get(0)).isInstanceOf(HospitalRecordResponse.class);
        assertThat(result.hospitalRecords().get(1)).isInstanceOf(HospitalRecordResponse.class);
    }

    @Test
    void 펫_아이디로_기록이_없는_달_정상_검색() {
        // GIVEN
        Pet pet = Pet.builder().id(1L).build();
        String token = "token";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(tokenService.getUserId(token)).thenReturn(1L);
        when(userPetRepository.existsByUserIdAndPetId(tokenService.getUserId(token),
                pet.getId())).thenReturn(true);
        when(scheduleRepository.findHospitalRecordAllByYearAndMonth(pet.getId(), 2024,
                11)).thenReturn(new ArrayList<>());
        when(scheduleRepository.findWeightRecordAllByYearAndMonth(pet.getId(), 2024,
                11)).thenReturn(new ArrayList<>());
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // WHEN
        ScheduleMonthlyResponse result = scheduleService.getMonthly(pet.getId(), 2024, 11, request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.hospitalRecords()).isEmpty();
        assertThat(result.weightRecords()).isEmpty();
    }

    @Test
    void 펫_아이디가_존재하지_않을_경우_예외_발생() {
        // GIVEN
        Pet pet = Pet.builder().id(1L).build();
        String token = "token";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(petRepository.existsById(pet.getId())).thenReturn(false);

        // WHEN, THEN
        assertThatThrownBy(
                () -> scheduleService.getMonthly(pet.getId(), 2024, 10, request)).isInstanceOf(
                PetNotFoundException.class);
    }

    @Test
    void 해당_펫_그룹에_속해_있는_유저가_아닐_경우_예외_발생() {
        // GIVEN
        Pet pet = Pet.builder().id(1L).build();
        String token = "token";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(petRepository.existsById(pet.getId())).thenReturn(true);
        when(tokenService.getUserId(token)).thenReturn(1L);
        when(userPetRepository.existsByUserIdAndPetId(tokenService.getUserId(token),
                pet.getId())).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // WHEN, THEN
        assertThatThrownBy(
                () -> scheduleService.getMonthly(pet.getId(), 2024, 10, request)).isInstanceOf(
                UserInformationNotFoundException.class);
    }
}
