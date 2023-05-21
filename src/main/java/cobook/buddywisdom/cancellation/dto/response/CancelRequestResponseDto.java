package cobook.buddywisdom.cancellation.dto.response;

import java.time.LocalDateTime;

import cobook.buddywisdom.cancellation.domain.CancelRequest;

public record CancelRequestResponseDto (
	long id,
	long menteeScheduleId,
	long senderId,
	long receiverId,
	String reason,
	boolean confirmYn,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static CancelRequestResponseDto from(CancelRequest cancelRequest) {
		return new CancelRequestResponseDto(
			cancelRequest.getId(),
			cancelRequest.getMenteeScheduleId(),
			cancelRequest.getSenderId(),
			cancelRequest.getReceiverId(),
			cancelRequest.getReason(),
			cancelRequest.isConfirmYn(),
			cancelRequest.getCreatedAt(),
			cancelRequest.getUpdatedAt()
		);
	}
}
