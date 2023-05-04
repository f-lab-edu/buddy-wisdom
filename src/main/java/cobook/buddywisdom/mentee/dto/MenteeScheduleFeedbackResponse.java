package cobook.buddywisdom.mentee.dto;

import java.time.LocalDateTime;

import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import jakarta.annotation.Nullable;

public record MenteeScheduleFeedbackResponse(
	Long id,
	LocalDateTime possibleDateTime,
	@Nullable
	Long feedbackId,
	@Nullable
	String menteeFeedBack,
	@Nullable
	String coachFeedback
){
	public static MenteeScheduleFeedbackResponse from(MenteeScheduleFeedback menteeScheduleFeedback) {
		return new MenteeScheduleFeedbackResponse(
			menteeScheduleFeedback.getId(),
			menteeScheduleFeedback.getPossibleDateTime(),
			menteeScheduleFeedback.getFeedbackId(),
			menteeScheduleFeedback.getMenteeFeedback(),
			menteeScheduleFeedback.getCoachFeedback()
		);
	}
}
