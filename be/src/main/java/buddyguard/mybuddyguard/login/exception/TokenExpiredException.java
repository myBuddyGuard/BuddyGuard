package buddyguard.mybuddyguard.login.exception;

import buddyguard.mybuddyguard.exception.FilterException;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends FilterException {

    public TokenExpiredException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
