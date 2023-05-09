package cobook.buddywisdom.mentee.dto.response;

import java.time.LocalDateTime;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;

public record MenteeMonthlyScheduleResponseDto(
	Long id,
	Long coachingScheduleId,
	boolean cancelYn,
	LocalDateTime possibleDateTime
) {
	public static MenteeMonthlyScheduleResponseDto from(MenteeMonthlySchedule menteeMonthlySchedule) {
		return new MenteeMonthlyScheduleResponseDto(
			menteeMonthlySchedule.getId(),
			menteeMonthlySchedule.getCoachingScheduleId(),
			menteeMonthlySchedule.isCancelYn(),
			menteeMonthlySchedule.getPossibleDateTime()
		);
	}
}
