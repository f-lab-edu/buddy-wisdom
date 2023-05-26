package cobook.buddywisdom.global.vo;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEventDetails {

	private Long senderId;
	private Long receiverId;
	private LocalDateTime scheduleDateTime;

	public static ScheduleEventDetails of(Long senderId, Long receiverId, LocalDateTime scheduleDateTime) {
		ScheduleEventDetails scheduleEventDetails = new ScheduleEventDetails();
		scheduleEventDetails.senderId = senderId;
		scheduleEventDetails.receiverId = receiverId;
		scheduleEventDetails.scheduleDateTime = scheduleDateTime;
		return scheduleEventDetails;
	}

}
