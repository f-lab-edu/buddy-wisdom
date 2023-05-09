package cobook.buddywisdom.mentee.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class DuplicatedMenteeScheduleException extends RuntimeException {
	public DuplicatedMenteeScheduleException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
