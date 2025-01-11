package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class PetNotFoundException extends BusinessException {

    public PetNotFoundException() {
        super(HttpStatus.NOT_FOUND, "펫 정보를 찾을 수 없습니다.");
    }
}
