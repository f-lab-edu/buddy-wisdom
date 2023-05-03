package cobook.buddywisdom.global.exception;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final String status;
	private final String message;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorMessage errorMessage) {
		return ResponseEntity
			.status(errorMessage.getStatus().value())
			.body(ErrorResponse.builder()
				.status(errorMessage.getStatus().toString())
				.message(errorMessage.getMessage())
				.build()
			);
	}
}
