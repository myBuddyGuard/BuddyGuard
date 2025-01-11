package buddyguard.mybuddyguard.feed.aop;

import buddyguard.mybuddyguard.exception.PetNotFoundException;
import buddyguard.mybuddyguard.exception.UserInformationNotFoundException;
import buddyguard.mybuddyguard.exception.UserPetGroupException;
import buddyguard.mybuddyguard.login.repository.UserRepository;
import buddyguard.mybuddyguard.pet.repository.PetRepository;
import buddyguard.mybuddyguard.pet.repository.UserPetRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserPetGroupValidationAspect {

    private final UserPetRepository userPetRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;


    @Before("@annotation(buddyguard.mybuddyguard.feed.aop.UserPetGroupValidation)")
    public void checkUserPetConnection(JoinPoint joinPoint) {
        // 파라미터의 순서를 맞춰줘야 하는 문제점 존재..
        Long petId = (Long) joinPoint.getArgs()[0];
        Long userId = (Long) joinPoint.getArgs()[1];

        if (!petRepository.existsById(petId)) {
            throw new PetNotFoundException();
        }
        if (!userRepository.existsById(userId)) {
            throw new UserInformationNotFoundException();
        }
        if (!userPetRepository.existsByUserIdAndPetId(userId, petId)) {
            throw new UserPetGroupException();
        }
    }
}
