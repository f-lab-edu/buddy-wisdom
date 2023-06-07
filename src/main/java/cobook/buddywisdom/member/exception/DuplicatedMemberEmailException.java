package cobook.buddywisdom.member.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class DuplicatedMemberEmailException extends RuntimeException{

    public DuplicatedMemberEmailException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
