package cobook.buddywisdom.cancellation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CancelRequestDto (
	@NotNull(message = "취소하려는 일정을 선택해 주세요.")
	Long menteeScheduleId,
	@NotBlank(message = "취소 사유를 입력해 주세요.")
	String reason
) {
}
