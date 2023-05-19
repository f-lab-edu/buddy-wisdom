package cobook.buddywisdom.cancellation.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class ConfirmedCancelRequestException extends RuntimeException {
	public ConfirmedCancelRequestException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
