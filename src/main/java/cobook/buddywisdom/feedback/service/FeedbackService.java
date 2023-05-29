package cobook.buddywisdom.feedback.service;

import static cobook.buddywisdom.global.vo.MemberApiType.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.feedback.domain.Feedback;
import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.exception.NotFoundFeedbackException;
import cobook.buddywisdom.feedback.mapper.FeedbackMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.vo.MemberApiType;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;

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

	public void updateFeedback(long menteeScheduleId, String feedback, MemberApiType apiType) {
		feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.orElseThrow(() -> new NotFoundFeedbackException(ErrorMessage.NOT_FOUND_FEEDBACK));

		if (COACHES.equals(apiType)) {
			feedbackMapper.updateCoachFeedbackByMenteeScheduleId(menteeScheduleId, feedback);
		} else if (MENTEES.equals(apiType)) {
			feedbackMapper.updateMenteeFeedbackByMenteeScheduleId(menteeScheduleId, feedback);
		}
	}
}
