package cobook.buddywisdom.global.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEventDetails {

	private Long senderId;
	private Long receiverId;
	private String template;

	public static ScheduleEventDetails of(Long senderId, Long receiverId, String template) {
		ScheduleEventDetails scheduleEventDetails = new ScheduleEventDetails();
		scheduleEventDetails.senderId = senderId;
		scheduleEventDetails.receiverId = receiverId;
		scheduleEventDetails.template = template;
		return scheduleEventDetails;
	}

}
