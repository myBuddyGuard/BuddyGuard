package buddyguard.mybuddyguard.schedule.service;

import buddyguard.mybuddyguard.exception.RecordNotFoundException;
import buddyguard.mybuddyguard.hospital.controller.reponse.HospitalRecordResponse;
import buddyguard.mybuddyguard.hospital.entity.HospitalRecord;
import buddyguard.mybuddyguard.hospital.mapper.HospitalRecordMapper;
import buddyguard.mybuddyguard.jwt.service.TokenService;
import buddyguard.mybuddyguard.jwt.utils.TokenUtility;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import buddyguard.mybuddyguard.schedule.controller.response.ScheduleMonthlyResponse;
import buddyguard.mybuddyguard.schedule.repository.ScheduleRepository;
import buddyguard.mybuddyguard.weight.contoller.response.WeightResponse;
import buddyguard.mybuddyguard.weight.entity.Weight;
import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.weight.mapper.WeightMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserPetRepository userPetRepository;
    private final PetRepository petRepository;
    private final TokenService tokenService;

    public ScheduleService(ScheduleRepository scheduleRepository,
            UserPetRepository userPetRepository, PetRepository petRepository,
            TokenService tokenService) {
        this.scheduleRepository = scheduleRepository;
        this.userPetRepository = userPetRepository;
        this.petRepository = petRepository;
        this.tokenService = tokenService;
    }

    public ScheduleMonthlyResponse getMonthly(Long petId, int year, int month,
            HttpServletRequest request) {
        // 1. 검증 파트
        // pet id 검증 (존재하는 pet인가?)
        if (!petRepository.existsById(petId)) {
            throw new PetNotFoundException();
        }

        // user id 검증 (펫 그룹에 속해있는 유저의 조회인가?)
        String accessToken = TokenUtility.deletePrefixToken(request.getHeader("Authorization"));
        Long userId = tokenService.getUserId(accessToken);
        if (!userPetRepository.existsByUserIdAndPetId(userId, petId)) {
            throw new UserInformationNotFoundException();
        }

        // 2. 실제 데이터 조회해와서 response로 만들기 (년도 && 월별 조회)
        List<Weight> weights = scheduleRepository.findWeightRecordAllByYearAndMonth(
                petId, year, month);
        List<HospitalRecord> hospitals = scheduleRepository.findHospitalRecordAllByYearAndMonth(
                petId, year, month);

        List<WeightResponse> weightResponses = weights.isEmpty()
                ? new ArrayList<>() : WeightMapper.toResponseList(weights);

        List<HospitalRecordResponse> hospitalResponses = hospitals.isEmpty()
                ? new ArrayList<>() : HospitalRecordMapper.toResponseList(hospitals);

        return new ScheduleMonthlyResponse(weightResponses, hospitalResponses);
    }
}
