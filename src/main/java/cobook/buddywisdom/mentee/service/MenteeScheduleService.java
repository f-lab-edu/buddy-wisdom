package cobook.buddywisdom.mentee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequest;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;

@Service
@Transactional(readOnly = true)
public class MenteeScheduleService {

	private final MenteeScheduleMapper menteeScheduleMapper;

	public MenteeScheduleService(MenteeScheduleMapper menteeScheduleMapper) {
		this.menteeScheduleMapper = menteeScheduleMapper;
	}

	public List<MenteeMonthlyScheduleResponse> getMenteeMonthlySchedule(Long menteeId, MenteeMonthlyScheduleRequest request) {
		MenteeMonthlySchedule menteeMonthlySchedule =
			menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(menteeId, request.startDateTime(), request.endDateTime());

		return Optional.ofNullable(menteeMonthlySchedule)
			.map(MenteeMonthlyScheduleResponse::from)
			.stream().toList();
	}

	public MenteeScheduleFeedbackResponse getMenteeScheduleFeedback(Long menteeId, Long coachingScheduleId) {
		MenteeScheduleFeedback menteeScheduleFeedback = menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		return MenteeScheduleFeedbackResponse.from(menteeScheduleFeedback);
	}
}
