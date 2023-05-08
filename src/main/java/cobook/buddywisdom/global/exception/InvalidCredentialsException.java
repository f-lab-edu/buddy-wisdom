package cobook.buddywisdom.global.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
