package buddyguard.mybuddyguard.login.exception;

import buddyguard.mybuddyguard.exception.FilterException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends FilterException {

    public TokenNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
