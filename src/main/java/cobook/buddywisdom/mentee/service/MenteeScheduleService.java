package cobook.buddywisdom.mentee.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
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
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;
import cobook.buddywisdom.relationship.domain.CoachingRelationship;
import cobook.buddywisdom.relationship.service.CoachingRelationshipService;

@Service
@Transactional(readOnly = true)
public class MenteeScheduleService {

	private final MenteeScheduleMapper menteeScheduleMapper;
	private final CoachScheduleService coachScheduleService;
	private final CoachingRelationshipService coachingRelationshipService;

	public MenteeScheduleService(MenteeScheduleMapper menteeScheduleMapper, CoachScheduleService coachScheduleService,
		CoachingRelationshipService coachingRelationshipService) {
		this.menteeScheduleMapper = menteeScheduleMapper;
		this.coachScheduleService = coachScheduleService;
		this.coachingRelationshipService = coachingRelationshipService;
	}

	public List<MenteeMonthlyScheduleResponseDto> getMenteeMonthlySchedule(Long menteeId, MenteeMonthlyScheduleRequestDto request) {
		List<MenteeMonthlySchedule> menteeMonthlyScheduleList =
			menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(menteeId, request.startDateTime(), request.endDateTime());

		return Optional.ofNullable(menteeMonthlyScheduleList)
			.map(m -> m.stream().map(MenteeMonthlyScheduleResponseDto::from).toList())
			.orElseGet(Collections::emptyList);
	}

	public MenteeScheduleFeedbackResponseDto getMenteeScheduleFeedback(Long menteeId, Long coachingScheduleId) {
		MenteeScheduleFeedback menteeScheduleFeedback = menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		return MenteeScheduleFeedbackResponseDto.from(menteeScheduleFeedback);
	}

	public List<MyCoachScheduleResponseDto> getMyCoachSchedule(Long menteeId) {
		CoachingRelationship coachingRelationship = coachingRelationshipService.getCoachingRelationshipByMenteeId(menteeId);

		List<CoachSchedule> coachScheduleList =
			coachScheduleService.getAllCoachingSchedule(coachingRelationship.getCoachId());

		return Optional.ofNullable(coachScheduleList)
			.map(m -> m.stream().map(MyCoachScheduleResponseDto::from).toList())
			.orElseGet(Collections::emptyList);
	}

	@Transactional
	public MenteeScheduleResponseDto saveMenteeSchedule(Long menteeId, Long coachingScheduleId) {
		coachScheduleService.getCoachSchedule(coachingScheduleId,false);

		checkMenteeScheduleNotExist(coachingScheduleId);

		MenteeSchedule menteeSchedule = MenteeSchedule.of(coachingScheduleId, menteeId);
		menteeScheduleMapper.save(menteeSchedule);

		coachScheduleService.updateMatchYn(coachingScheduleId, true);

		return MenteeScheduleResponseDto.from(menteeSchedule);
	}

	public void checkMenteeScheduleNotExist(Long coachingScheduleId) {
		Optional<MenteeSchedule> menteeSchedule = menteeScheduleMapper.findByCoachingScheduleId(coachingScheduleId);

		if (menteeSchedule.isEmpty()) {
			return;
		}

		throw new DuplicatedMenteeScheduleException(ErrorMessage.DUPLICATED_MENTEE_SCHEDULE);
	}
}
