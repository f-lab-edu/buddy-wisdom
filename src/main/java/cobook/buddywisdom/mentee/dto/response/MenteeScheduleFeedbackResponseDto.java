package cobook.buddywisdom.mentee.dto.response;

import java.time.LocalDateTime;

import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import jakarta.annotation.Nullable;

public record MenteeScheduleFeedbackResponseDto(
	long coachingScheduleId,
	LocalDateTime possibleDateTime,
	@Nullable
	long feedbackId,
	@Nullable
	String menteeFeedBack,
	@Nullable
	String coachFeedback
){
	public static MenteeScheduleFeedbackResponseDto from(MenteeScheduleFeedback menteeScheduleFeedback) {
		return new MenteeScheduleFeedbackResponseDto(
			menteeScheduleFeedback.getCoachingScheduleId(),
			menteeScheduleFeedback.getPossibleDateTime(),
			menteeScheduleFeedback.getFeedbackId(),
			menteeScheduleFeedback.getMenteeFeedback(),
			menteeScheduleFeedback.getCoachFeedback()
		);
	}
}
