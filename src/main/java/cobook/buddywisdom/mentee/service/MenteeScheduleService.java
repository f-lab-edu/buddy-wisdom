package cobook.buddywisdom.mentee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;

@Service
public class MenteeScheduleService {

	private final MenteeScheduleMapper menteeScheduleMapper;

	public MenteeScheduleService(MenteeScheduleMapper menteeScheduleMapper) {
		this.menteeScheduleMapper = menteeScheduleMapper;
	}

	public Optional<List<MenteeMonthlyScheduleResponse>> getMenteeMonthlySchedule(Long menteeId, String date) {
		MenteeMonthlySchedule menteeMonthlySchedule = menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(menteeId, date);

		return Optional.of(Optional.ofNullable(menteeMonthlySchedule)
			.map(MenteeMonthlyScheduleResponse::from)
			.stream().toList());
	}

	public MenteeScheduleFeedbackResponse getMenteeScheduleFeedback(Long menteeId, Long coachingScheduleId) {
		MenteeScheduleFeedback menteeScheduleFeedback = menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(menteeId, coachingScheduleId)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_MENTEE_SCHEDULE));

		return MenteeScheduleFeedbackResponse.from(menteeScheduleFeedback);
	}
}
