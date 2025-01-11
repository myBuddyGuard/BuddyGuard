package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BusinessException {

    public InvalidRequestException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");
    }
}
