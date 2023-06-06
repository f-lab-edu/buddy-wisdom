package cobook.buddywisdom.member.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class AdminDeletionNotAllowedException extends RuntimeException {
    public AdminDeletionNotAllowedException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
