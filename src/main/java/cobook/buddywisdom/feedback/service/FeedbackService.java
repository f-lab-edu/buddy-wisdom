package cobook.buddywisdom.feedback.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.exception.NotFoundFeedbackException;
import cobook.buddywisdom.feedback.mapper.FeedbackMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;

@Service
public class FeedbackService {

	private final FeedbackMapper feedbackMapper;

	public FeedbackService(FeedbackMapper feedbackMapper) {
		this.feedbackMapper = feedbackMapper;
	}

	public Optional<FeedbackResponseDto> getFeedback(long menteeScheduleId) {
		return feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.map(FeedbackResponseDto::from);
	}

	public void updateFeedbackByCoach(long menteeScheduleId, String feedback) {
		feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.orElseThrow(() -> new NotFoundFeedbackException(ErrorMessage.NOT_FOUND_FEEDBACK));

		feedbackMapper.updateCoachFeedbackByMenteeScheduleId(menteeScheduleId, feedback);
	}

	public void updateFeedbackByMentee(long menteeScheduleId, String feedback) {
		feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.orElseThrow(() -> new NotFoundFeedbackException(ErrorMessage.NOT_FOUND_FEEDBACK));

		feedbackMapper.updateMenteeFeedbackByMenteeScheduleId(menteeScheduleId, feedback);

	}
}
