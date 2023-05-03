package cobook.buddywisdom.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundMenteeScheduleException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundMenteeScheduleException(NotFoundMenteeScheduleException exception) {
		log.error("NotFoundMenteeScheduleException : ", exception);
		return ErrorResponse.toResponseEntity(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
		log.error("ConstraintViolationException : ", exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage()));
	}
}
