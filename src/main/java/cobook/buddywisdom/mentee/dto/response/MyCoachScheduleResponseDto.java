package cobook.buddywisdom.mentee.dto.response;

import java.time.LocalDateTime;

import cobook.buddywisdom.coach.domain.CoachSchedule;

public record MyCoachScheduleResponseDto(
	long id,
	long coachId,
	LocalDateTime possibleDateTime,
	boolean matchYn
) {
	public static MyCoachScheduleResponseDto from(CoachSchedule coachSchedule) {
		return new MyCoachScheduleResponseDto(
			coachSchedule.getId(),
			coachSchedule.getCoachId(),
			coachSchedule.getPossibleDateTime(),
			coachSchedule.isMatchYn()
		);
	}
}
