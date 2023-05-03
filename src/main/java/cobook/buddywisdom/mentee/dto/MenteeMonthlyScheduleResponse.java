package cobook.buddywisdom.mentee.dto;

import java.time.LocalDateTime;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;

public record MenteeMonthlyScheduleResponse(
	Long id,
	Long coachingScheduleId,
	boolean cancelYn,
	LocalDateTime possibleDateTime
) {
	public static MenteeMonthlyScheduleResponse from(MenteeMonthlySchedule menteeMonthlySchedule) {
		return new MenteeMonthlyScheduleResponse(
			menteeMonthlySchedule.getId(),
			menteeMonthlySchedule.getCoachingScheduleId(),
			menteeMonthlySchedule.isCancelYn(),
			menteeMonthlySchedule.getPossibleDateTime()
		);
	}
}
