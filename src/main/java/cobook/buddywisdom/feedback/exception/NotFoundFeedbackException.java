package cobook.buddywisdom.feedback.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundFeedbackException extends RuntimeException {
	public NotFoundFeedbackException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
