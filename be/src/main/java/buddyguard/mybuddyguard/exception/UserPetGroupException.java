package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class UserPetGroupException extends BusinessException{

    public UserPetGroupException() {
        super(HttpStatus.NOT_FOUND, "해당 유저와 펫 그룹은 존재하지 않습니다.");
    }
}
