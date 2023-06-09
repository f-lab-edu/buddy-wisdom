package cobook.buddywisdom.mentee.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteeScheduleFeedback {
	private Long coachingScheduleId;
	private Long feedbackId;
	private String coachFeedback;
	private String menteeFeedback;
	private LocalDateTime possibleDateTime;

	public static MenteeScheduleFeedback of(Long coachingScheduleId, Long feedbackId, String coachFeedback, String menteeFeedback, LocalDateTime possibleDateTime) {
		MenteeScheduleFeedback menteeScheduleFeedback = new MenteeScheduleFeedback();
		menteeScheduleFeedback.coachingScheduleId = coachingScheduleId;
		menteeScheduleFeedback.feedbackId = feedbackId;
		menteeScheduleFeedback.coachFeedback = coachFeedback;
		menteeScheduleFeedback.menteeFeedback = menteeFeedback;
		menteeScheduleFeedback.possibleDateTime = possibleDateTime;
		return menteeScheduleFeedback;
	}
}
