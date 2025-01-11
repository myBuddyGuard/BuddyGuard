package buddyguard.mybuddyguard.invitation.exception;

import buddyguard.mybuddyguard.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvitationLinkExpiredException extends BusinessException {

    public InvitationLinkExpiredException() {
        super(HttpStatus.NOT_FOUND, "초대 링크가 만료되었습니다.");
    }
}
