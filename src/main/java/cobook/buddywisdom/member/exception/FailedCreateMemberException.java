package cobook.buddywisdom.member.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class FailedCreateMemberException extends RuntimeException {

    public FailedCreateMemberException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
