package buddyguard.mybuddyguard.exception.response;


import buddyguard.mybuddyguard.exception.BusinessException;

public record BusinessExceptionResponse(String exceptionClass, String errorMessage) {

    public static BusinessExceptionResponse from(BusinessException e) {
        String exceptionClass = e.getClass().getSimpleName();
        String errorMessage = e.getMessage();

        return new BusinessExceptionResponse(exceptionClass, errorMessage);
    }
}
