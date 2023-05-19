package cobook.buddywisdom.cancellation.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundCancelRequestException extends RuntimeException {
	public NotFoundCancelRequestException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
