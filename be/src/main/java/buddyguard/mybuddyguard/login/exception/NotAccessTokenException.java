package buddyguard.mybuddyguard.login.exception;

import buddyguard.mybuddyguard.exception.FilterException;
import org.springframework.http.HttpStatus;

public class NotAccessTokenException extends FilterException {

    public NotAccessTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
