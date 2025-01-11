package buddyguard.mybuddyguard.login.exception;

import buddyguard.mybuddyguard.exception.FilterException;
import org.springframework.http.HttpStatus;

public class LogoutException extends FilterException {

    public LogoutException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
