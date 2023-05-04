package cobook.buddywisdom.mentee.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteeMonthlySchedule {
	private Long id;
	private Long coachingScheduleId;
	private boolean cancelYn;
	private LocalDateTime possibleDateTime;

	public static MenteeMonthlySchedule of(Long coachingScheduleId, boolean cancelYn, LocalDateTime possibleDateTime) {
		MenteeMonthlySchedule menteeMonthlySchedule = new MenteeMonthlySchedule();
		menteeMonthlySchedule.coachingScheduleId = coachingScheduleId;
		menteeMonthlySchedule.cancelYn = cancelYn;
		menteeMonthlySchedule.possibleDateTime = possibleDateTime;
		return menteeMonthlySchedule;
	}
}
