package cobook.buddywisdom.mentee.dto.response;

import cobook.buddywisdom.mentee.domain.MenteeSchedule;

public record MenteeScheduleResponseDto (
	Long id,
	Long coachingScheduleId,
	Long menteeId,
	boolean cancelYn
) {
	public static MenteeScheduleResponseDto from(MenteeSchedule menteeSchedule) {
		return new MenteeScheduleResponseDto(
			menteeSchedule.getId(),
			menteeSchedule.getCoachingScheduleId(),
			menteeSchedule.getMenteeId(),
			menteeSchedule.isCancelYn()
		);
	}
}
