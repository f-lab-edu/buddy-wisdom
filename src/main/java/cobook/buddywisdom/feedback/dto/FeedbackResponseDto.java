package cobook.buddywisdom.feedback.dto;

import cobook.buddywisdom.feed.domain.Feed;
import cobook.buddywisdom.feedback.domain.Feedback;

public record FeedbackResponseDto (
	long menteeScheduleId,
	String coachFeedback,
	String menteeFeedback
) {
	public static FeedbackResponseDto from(Feedback feedback) {
		return new FeedbackResponseDto(
			feedback.getMenteeScheduleId(),
			feedback.getCoachFeedback(),
			feedback.getMenteeFeedback()
		);
	}
}
