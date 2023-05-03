package cobook.buddywisdom.mentee.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundMenteeScheduleException extends RuntimeException {
	public NotFoundMenteeScheduleException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
