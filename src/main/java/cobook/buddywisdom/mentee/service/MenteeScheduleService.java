package cobook.buddywisdom.mentee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.response.MenteeMonthlyScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleFeedbackResponseDto;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleResponseDto;
import cobook.buddywisdom.mentee.exception.DuplicatedMenteeScheduleException;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;

@Service
@Transactional(readOnly = true)
public class MenteeScheduleService {

	private final MenteeScheduleMapper menteeScheduleMapper;
	private final CoachScheduleService coachScheduleService;

	public MenteeScheduleService(MenteeScheduleMapper menteeScheduleMapper, CoachScheduleService coachScheduleService) {
		this.menteeScheduleMapper = menteeScheduleMapper;
		this.coachScheduleService = coachScheduleService;
	}

	public List<MenteeMonthlyScheduleResponseDto> getMenteeMonthlySchedule(Long menteeId, MenteeMonthlyScheduleRequestDto request) {
		MenteeMonthlySchedule menteeMonthlySchedule =
			menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(menteeId, request.startDateTime(), request.endDateTime());

		return Optional.ofNullable(menteeMonthlySchedule)
			.map(MenteeMonthlyScheduleResponseDto::from)
			.stream().toList();
	}

	public MenteeScheduleFeedbackResponseDto getMenteeScheduleFeedback(Long menteeId, Long coachingScheduleId) {
		MenteeScheduleFeedback menteeScheduleFeedback = menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		return MenteeScheduleFeedbackResponseDto.from(menteeScheduleFeedback);
	}

	@Transactional
	public MenteeScheduleResponseDto saveMenteeSchedule(Long menteeId, Long coachingScheduleId) {
		coachScheduleService.getCoachSchedule(coachingScheduleId);

		checkMenteeScheduleNotExist(menteeId, coachingScheduleId, false);

		MenteeSchedule menteeSchedule = MenteeSchedule.of(coachingScheduleId, menteeId);
		menteeScheduleMapper.save(menteeSchedule);

		return MenteeScheduleResponseDto.from(menteeSchedule);
	}

	private void checkMenteeScheduleNotExist(Long menteeId, Long coachingScheduleId, boolean cancelYn) {
		Optional<MenteeSchedule> menteeSchedule =
			menteeScheduleMapper.findByMenteeIdAndCoachingScheduleIdAndCancelYn(menteeId, coachingScheduleId, cancelYn);

		if (menteeSchedule.isEmpty()) {
			return;
		}

		throw new DuplicatedMenteeScheduleException(ErrorMessage.DUPLICATED_MENTEE_SCHEDULE);
	}
}
