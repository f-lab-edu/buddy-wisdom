package cobook.buddywisdom.mentee.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMenteeScheduleRequestDto (
	@NotNull(message = "기존 일정 정보를 선택해 주세요.")
	Long currentCoachingId,
	@NotNull(message = "변경하려는 일정 정보를 선택해 주세요.")
	Long newCoachingId
) {
}
