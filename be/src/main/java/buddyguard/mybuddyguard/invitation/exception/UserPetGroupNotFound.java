package buddyguard.mybuddyguard.invitation.exception;

import buddyguard.mybuddyguard.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserPetGroupNotFound extends BusinessException {

    public UserPetGroupNotFound() {
        super(HttpStatus.NOT_FOUND, "해당 펫 그룹이 존재하지 않습니다.");
    }
}
