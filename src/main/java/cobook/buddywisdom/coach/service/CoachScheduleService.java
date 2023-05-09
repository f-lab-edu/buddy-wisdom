package cobook.buddywisdom.coach.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.mapper.CoachScheduleMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;

@Service
@Transactional(readOnly = true)
public class CoachScheduleService {

	private final CoachScheduleMapper coachScheduleMapper;

	public CoachScheduleService(CoachScheduleMapper coachScheduleMapper) {
		this.coachScheduleMapper = coachScheduleMapper;
	}

	public CoachSchedule getCoachSchedule(Long id) {
		return coachScheduleMapper.findById(id)
			.orElseThrow(() -> new NotFoundMenteeScheduleException(ErrorMessage.NOT_FOUND_COACH_SCHEDULE));
	}

}
