package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class UserInformationNotFoundException extends BusinessException {

    public UserInformationNotFoundException() {
        super(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다.");
    }
}