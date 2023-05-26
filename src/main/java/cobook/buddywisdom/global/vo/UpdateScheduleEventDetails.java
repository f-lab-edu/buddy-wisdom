package cobook.buddywisdom.global.vo;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateScheduleEventDetails {

	private ScheduleEventDetails scheduleEventDetails;

	private LocalDateTime newCoachingDateTime;

	public static UpdateScheduleEventDetails of(Long senderId, Long receiverId, LocalDateTime currentCoachingDateTime,
									LocalDateTime newCoachingDateTime) {
		UpdateScheduleEventDetails updateScheduleEventDetails = new UpdateScheduleEventDetails();
		updateScheduleEventDetails.scheduleEventDetails = ScheduleEventDetails.of(senderId, receiverId, currentCoachingDateTime);
		updateScheduleEventDetails.newCoachingDateTime = newCoachingDateTime;
		return updateScheduleEventDetails;
	}

}
