package cobook.buddywisdom.mentee.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteeSchedule {
	private Long coachingScheduleId;
	private Long menteeId;

	public static MenteeSchedule of(Long coachingScheduleId, Long menteeId) {
		MenteeSchedule menteeSchedule = new MenteeSchedule();
		menteeSchedule.coachingScheduleId = coachingScheduleId;
		menteeSchedule.menteeId = menteeId;
		return menteeSchedule;
	}
}
