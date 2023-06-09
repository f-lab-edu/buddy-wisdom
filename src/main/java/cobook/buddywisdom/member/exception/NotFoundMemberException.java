package cobook.buddywisdom.member.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException(ErrorMessage errorMessage) {
            super(errorMessage.getMessage());
    }
}
