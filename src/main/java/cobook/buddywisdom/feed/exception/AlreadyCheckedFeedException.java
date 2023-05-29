package cobook.buddywisdom.feed.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class AlreadyCheckedFeedException extends RuntimeException {
	public AlreadyCheckedFeedException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
