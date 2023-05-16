package cobook.buddywisdom.mentee.dto.response;

import cobook.buddywisdom.mentee.domain.MenteeSchedule;

public record MenteeScheduleResponseDto (
	long coachingScheduleId,
	long menteeId
) {
	public static MenteeScheduleResponseDto from(MenteeSchedule menteeSchedule) {
		return new MenteeScheduleResponseDto(
			menteeSchedule.getCoachingScheduleId(),
			menteeSchedule.getMenteeId()
		);
	}
}
