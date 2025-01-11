package buddyguard.mybuddyguard.exception;

import org.springframework.http.HttpStatus;

public class AlreadyInGroupException extends BusinessException{

    public AlreadyInGroupException() {
        super(HttpStatus.BAD_REQUEST, "이미 참여중인 그룹입니다.");
    }
}
