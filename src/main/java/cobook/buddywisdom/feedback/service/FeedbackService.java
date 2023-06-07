package cobook.buddywisdom.feedback.service;

import static cobook.buddywisdom.global.vo.MessageTemplate.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.exception.NotFoundFeedbackException;
import cobook.buddywisdom.feedback.mapper.FeedbackMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.util.MessageUtil;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackMapper feedbackMapper;
	private final MenteeScheduleService menteeScheduleService;
	private final CoachScheduleService coachScheduleService;
	private final FeedMessageProducer feedMessageProducer;
	private final MessageUtil messageUtil;

	@Transactional(readOnly = true)
	public Optional<FeedbackResponseDto> getFeedback(long menteeScheduleId) {
		return feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.map(FeedbackResponseDto::from);
	}

	public void updateFeedbackByCoach(long coachId, long menteeScheduleId, String feedback) {
		checkIfFeedbackExists(menteeScheduleId);
		feedbackMapper.updateCoachFeedbackByMenteeScheduleId(menteeScheduleId, feedback);

		MenteeSchedule menteeSchedule = menteeScheduleService.getMenteeScheduleByScheduleId(menteeScheduleId);
		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(menteeScheduleId, true);
		processEventProducer(coachId, menteeSchedule.getMenteeId(), coachSchedule.getPossibleDateTime());
	}

	public void updateFeedbackByMentee(long menteeId, long menteeScheduleId, String feedback) {
		checkIfFeedbackExists(menteeScheduleId);
		feedbackMapper.updateMenteeFeedbackByMenteeScheduleId(menteeScheduleId, feedback);

		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(menteeScheduleId, true);
		processEventProducer(menteeId, coachSchedule.getCoachId(), coachSchedule.getPossibleDateTime());
	}

	private void processEventProducer(long senderId, long receiverId, LocalDateTime localDateTime) {
		String dateTime = messageUtil.convertToString(localDateTime);

		feedMessageProducer.produceScheduleEvent(senderId, receiverId, () ->
			MessageFormat.format(UPDATE_FEEDBACK, dateTime));
	}

	private void checkIfFeedbackExists(long menteeScheduleId) {
		feedbackMapper.findByMenteeScheduleId(menteeScheduleId)
			.orElseThrow(() -> new NotFoundFeedbackException(ErrorMessage.NOT_FOUND_FEEDBACK));
	}
}
