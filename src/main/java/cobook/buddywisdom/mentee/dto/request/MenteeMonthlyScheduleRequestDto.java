package cobook.buddywisdom.mentee.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record MenteeMonthlyScheduleRequestDto(
	@NotNull(message = "startDateTime 값이 필요합니다.")
	LocalDateTime startDateTime,
	@NotNull(message = "endDateTime 값이 필요합니다.")
	LocalDateTime endDateTime
) {
}
