package buddyguard.mybuddyguard.invitation.exception;

import buddyguard.mybuddyguard.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidInvitationException extends BusinessException {

    public InvalidInvitationException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "초대 링크 생성 권한이 없습니다.");
    }

}
