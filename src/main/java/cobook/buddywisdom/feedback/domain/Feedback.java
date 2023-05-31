package cobook.buddywisdom.feedback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {
	private Long menteeScheduleId;
	private String coachFeedback;
	private String menteeFeedback;

	public static Feedback of(Long menteeScheduleId, String coachFeedback, String menteeFeedback) {
		Feedback feedback = new Feedback();
		feedback.menteeScheduleId = menteeScheduleId;
		feedback.coachFeedback = coachFeedback;
		feedback.menteeFeedback = menteeFeedback;
		return feedback;
	}
}
