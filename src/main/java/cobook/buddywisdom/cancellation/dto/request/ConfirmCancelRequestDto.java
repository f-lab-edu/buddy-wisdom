package cobook.buddywisdom.cancellation.dto.request;

import jakarta.validation.constraints.NotNull;

public record ConfirmCancelRequestDto(
	@NotNull(message = "취소 요청 정보가 필요합니다.")
	Long id,
	@NotNull(message = "스케줄 정보가 필요합니다.")
	Long menteeScheduleId
) {
}
