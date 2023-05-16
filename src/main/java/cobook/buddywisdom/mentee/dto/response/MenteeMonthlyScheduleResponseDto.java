package cobook.buddywisdom.mentee.dto.response;

import java.time.LocalDateTime;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;

public record MenteeMonthlyScheduleResponseDto(
	long coachingScheduleId,
	LocalDateTime possibleDateTime
) {
	public static MenteeMonthlyScheduleResponseDto from(MenteeMonthlySchedule menteeMonthlySchedule) {
		return new MenteeMonthlyScheduleResponseDto(
			menteeMonthlySchedule.getCoachingScheduleId(),
			menteeMonthlySchedule.getPossibleDateTime()
		);
	}
}
