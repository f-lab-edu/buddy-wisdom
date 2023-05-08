package cobook.buddywisdom.global.exception;

public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException(ErrorMessage errorMessage) {
            super(errorMessage.getMessage());
    }
}
