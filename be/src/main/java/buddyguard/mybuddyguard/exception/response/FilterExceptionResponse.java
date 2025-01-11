package buddyguard.mybuddyguard.exception.response;

import buddyguard.mybuddyguard.exception.FilterException;

public record FilterExceptionResponse(String exceptionClass, String errorMessage) {

    public static FilterExceptionResponse from(FilterException e) {
        String exceptionClass = e.getClass().getSimpleName();
        String errorMessage = e.getMessage();

        return new FilterExceptionResponse(exceptionClass, errorMessage);
    }
}
