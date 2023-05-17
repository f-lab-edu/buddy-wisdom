package cobook.buddywisdom.mentee.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotAllowedUpdateException extends RuntimeException {
	public NotAllowedUpdateException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
