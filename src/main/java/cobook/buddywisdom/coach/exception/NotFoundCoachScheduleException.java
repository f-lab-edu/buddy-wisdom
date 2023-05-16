package cobook.buddywisdom.coach.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundCoachScheduleException extends RuntimeException {
	public NotFoundCoachScheduleException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
