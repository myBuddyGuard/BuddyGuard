package buddyguard.mybuddyguard.exception;

import buddyguard.mybuddyguard.exception.response.BusinessExceptionResponse;
import buddyguard.mybuddyguard.exception.response.FilterExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        BusinessExceptionResponse response = BusinessExceptionResponse.from(e);

        log.error(response.toString());

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(FilterException.class)
    public ResponseEntity<?> handleFilterException(FilterException e) {
        FilterExceptionResponse response = FilterExceptionResponse.from(e);

        log.error(response.toString());

        return new ResponseEntity<>(response, e.getHttpStatus());
    }
}
