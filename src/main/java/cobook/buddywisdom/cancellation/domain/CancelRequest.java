package cobook.buddywisdom.cancellation.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelRequest {
	private Long id;
	private Long menteeScheduleId;
	private Long senderId;
	private Long receiverId;
	private String reason;
	private boolean confirmYn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static CancelRequest of(Long id, Long menteeScheduleId, Long senderId, Long receiverId, String reason,
									boolean confirmYn, LocalDateTime createdAt, LocalDateTime updatedAt) {
		CancelRequest cancelRequest = new CancelRequest();
		cancelRequest.id = id;
		cancelRequest.menteeScheduleId = menteeScheduleId;
		cancelRequest.senderId = senderId;
		cancelRequest.receiverId = receiverId;
		cancelRequest.reason = reason;
		cancelRequest.confirmYn = confirmYn;
		cancelRequest.createdAt = createdAt;
		cancelRequest.updatedAt = updatedAt;
		return cancelRequest;
	}

	public static CancelRequest requestOf(Long menteeScheduleId, Long senderId, Long receiverId, String reason) {
		CancelRequest cancelRequest = new CancelRequest();
		cancelRequest.menteeScheduleId = menteeScheduleId;
		cancelRequest.senderId = senderId;
		cancelRequest.receiverId = receiverId;
		cancelRequest.reason = reason;
		return cancelRequest;
	}
}
