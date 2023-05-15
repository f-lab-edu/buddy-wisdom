package cobook.buddywisdom.relationship.exception;

import cobook.buddywisdom.global.exception.ErrorMessage;

public class NotFoundRelationshipException extends RuntimeException {
	public NotFoundRelationshipException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
