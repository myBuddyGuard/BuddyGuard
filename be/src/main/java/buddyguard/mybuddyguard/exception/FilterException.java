package buddyguard.mybuddyguard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class FilterException extends AuthenticationException {

    private final HttpStatus httpStatus;

    public FilterException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
