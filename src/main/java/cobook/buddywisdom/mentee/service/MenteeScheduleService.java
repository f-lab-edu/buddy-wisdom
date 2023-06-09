package cobook.buddywisdom.mentee.service;

import static cobook.buddywisdom.global.vo.MessageTemplate.*;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.util.MessageUtil;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.response.MenteeMonthlyScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleFeedbackResponseDto;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MyCoachScheduleResponseDto;
import cobook.buddywisdom.mentee.exception.DuplicatedMenteeScheduleException;
import cobook.buddywisdom.mentee.exception.NotAllowedUpdateException;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import cobook.buddywisdom.relationship.domain.CoachingRelationship;
import cobook.buddywisdom.relationship.service.CoachingRelationshipService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenteeScheduleService {

	private final MenteeScheduleMapper menteeScheduleMapper;
	private final CoachScheduleService coachScheduleService;
	private final CoachingRelationshipService coachingRelationshipService;
	private final FeedMessageProducer feedMessageProducer;
	private final MessageUtil messageUtil;

	public List<MenteeMonthlyScheduleResponseDto> getMenteeMonthlySchedule(long menteeId, MenteeMonthlyScheduleRequestDto request) {
		List<MenteeMonthlySchedule> menteeMonthlyScheduleList =
			menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(menteeId, request.startDateTime(), request.endDateTime());

		return Optional.ofNullable(menteeMonthlyScheduleList)
			.stream()
			.flatMap(List::stream)
			.map(MenteeMonthlyScheduleResponseDto::from)
			.toList();
	}

	public MenteeScheduleFeedbackResponseDto getMenteeScheduleFeedback(long menteeId, long coachingScheduleId) {
		MenteeScheduleFeedback menteeScheduleFeedback = menteeScheduleMapper.findWithFeedbackByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		return MenteeScheduleFeedbackResponseDto.from(menteeScheduleFeedback);
	}

	public MenteeSchedule getMenteeSchedule(long menteeId, long coachingScheduleId) {
		return menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));
	}

	public MenteeSchedule getMenteeScheduleByScheduleId(long coachingScheduleId) {
		return menteeScheduleMapper.findByCoachingScheduleId(coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));
	}

	public List<MyCoachScheduleResponseDto> getMyCoachSchedule(long menteeId) {
		CoachingRelationship coachingRelationship = coachingRelationshipService.getCoachingRelationshipByMenteeId(menteeId);

		List<CoachSchedule> coachScheduleList =
			coachScheduleService.getAllCoachingSchedule(coachingRelationship.getCoachId());

		return Optional.ofNullable(coachScheduleList)
			.stream()
			.flatMap(List::stream)
			.map(MyCoachScheduleResponseDto::from)
			.toList();
	}

	@Transactional
	public MenteeScheduleResponseDto saveMenteeSchedule(long menteeId, long coachingScheduleId) {
		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(coachingScheduleId,false);

		checkMenteeScheduleNotExist(coachingScheduleId);

		MenteeSchedule menteeSchedule = MenteeSchedule.of(coachingScheduleId, menteeId);
		menteeScheduleMapper.save(menteeSchedule);
		coachScheduleService.updateMatchYn(coachingScheduleId, true);

		String dateTime = messageUtil.convertToString(coachSchedule.getPossibleDateTime());
		feedMessageProducer.produceScheduleEvent(menteeId, coachSchedule.getCoachId(), () ->
			MessageFormat.format(CREATE_SCHEDULE, dateTime));

		return MenteeScheduleResponseDto.from(menteeSchedule);
	}

	public void checkMenteeScheduleNotExist(long coachingScheduleId) {
		Optional<MenteeSchedule> menteeSchedule = menteeScheduleMapper.findByCoachingScheduleId(coachingScheduleId);

		if (menteeSchedule.isEmpty()) {
			return;
		}

		throw new DuplicatedMenteeScheduleException(ErrorMessage.DUPLICATED_MENTEE_SCHEDULE);
	}

	@Transactional
	public void updateMenteeSchedule(long menteeId, long currentCoachingId, long newCoachingId) {
		CoachSchedule currentCoachSchedule = getScheduleIfUpdatePossible(currentCoachingId);

		CoachSchedule newCoachSchedule = coachScheduleService.getCoachSchedule(newCoachingId, false);

		menteeScheduleMapper.updateCoachingScheduleId(currentCoachingId, newCoachingId);

		coachScheduleService.updateMatchYn(currentCoachingId, false);
		coachScheduleService.updateMatchYn(newCoachingId, true);

		String currentDateTime = messageUtil.convertToString(currentCoachSchedule.getPossibleDateTime());
		String newDateTime = messageUtil.convertToString(newCoachSchedule.getPossibleDateTime());
		feedMessageProducer.produceScheduleEvent(menteeId, currentCoachSchedule.getCoachId(), () ->
			MessageFormat.format(UPDATE_SCHEDULE, currentDateTime, newDateTime));
	}

	public CoachSchedule getScheduleIfUpdatePossible(long coachScheduleId) {
		CoachSchedule currentCoachSchedule = coachScheduleService.getCoachSchedule(coachScheduleId, true);

		LocalDate date = LocalDate.from(currentCoachSchedule.getPossibleDateTime());

		if (!LocalDate.now().isBefore(date)) {
			throw new NotAllowedUpdateException(ErrorMessage.NOT_ALLOWED_UPDATE_SCHEDULE);
		}

		return currentCoachSchedule;
	}

	@Transactional
	public void deleteMenteeSchedule(long menteeScheduleId) {
		MenteeSchedule menteeSchedule = menteeScheduleMapper.findByCoachingScheduleId(menteeScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		menteeScheduleMapper.deleteByCoachingScheduleId(menteeSchedule.getCoachingScheduleId());
	}
}
