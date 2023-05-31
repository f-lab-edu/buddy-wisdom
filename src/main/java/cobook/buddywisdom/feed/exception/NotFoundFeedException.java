package cobook.buddywisdom.feed.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundFeedException extends RuntimeException {
	public NotFoundFeedException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
