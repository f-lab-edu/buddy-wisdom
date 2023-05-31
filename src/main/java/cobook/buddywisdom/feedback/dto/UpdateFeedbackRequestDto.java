package cobook.buddywisdom.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFeedbackRequestDto (
	@NotNull(message = "스케줄 정보가 필요합니다.")
	Long menteeScheduleId,
	@NotBlank(message = "피드백을 입력해 주세요.")
	String feedback
) {
}
