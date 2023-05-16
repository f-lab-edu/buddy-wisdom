package cobook.buddywisdom.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.mentee.exception.DuplicatedMenteeScheduleException;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.relationship.exception.NotFoundRelationshipException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
		log.error("MethodArgumentNotValidException : ", exception);

		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors()
			.forEach(e -> errors.put(((FieldError)e).getField(), e.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(NotFoundCoachScheduleException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundCoachScheduleException(NotFoundCoachScheduleException exception) {
		log.error("NotFoundCoachScheduleException : ", exception);
		return ErrorResponse.toResponseEntity(ErrorMessage.NOT_FOUND_COACH_SCHEDULE);
	}

	@ExceptionHandler(NotFoundMenteeScheduleException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundMenteeScheduleException(NotFoundMenteeScheduleException exception) {
		log.error("NotFoundMenteeScheduleException : ", exception);
		return ErrorResponse.toResponseEntity(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE);
	}

	@ExceptionHandler(DuplicatedMenteeScheduleException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatedMenteeScheduleException(DuplicatedMenteeScheduleException exception) {
		log.error("DuplicatedMenteeScheduleException : ", exception);
		return ErrorResponse.toResponseEntity(ErrorMessage.DUPLICATED_MENTEE_SCHEDULE);
	}

	@ExceptionHandler(NotFoundRelationshipException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundRelationshipException(NotFoundRelationshipException exception) {
		log.error("NotFoundRelationshipException : ", exception);
		return ErrorResponse.toResponseEntity(ErrorMessage.NOT_FOUND_COACHING_RELATIONSHIP);
	}
}
