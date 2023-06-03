package cobook.buddywisdom.coach.service;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.coach.mapper.CoachScheduleMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CoachScheduleService {

    private final CoachScheduleMapper coachScheduleMapper;
    private static final int DEFAULT_DAYS = 8;

    public CoachScheduleService(CoachScheduleMapper coachScheduleMapper) {
        this.coachScheduleMapper = coachScheduleMapper;
    }

    public CoachSchedule getCoachSchedule(long id, boolean matchYn) {
        return coachScheduleMapper.findByIdAndMatchYn(id, matchYn)
                .orElseThrow(() -> new NotFoundCoachScheduleException(ErrorMessage.NOT_FOUND_COACH_SCHEDULE));
    }

    public List<CoachSchedule> getAllCoachingSchedule(long coachId) {
        LocalDate startDateTime = LocalDate.now();
        LocalDate endDateTime = startDateTime.plusDays(DEFAULT_DAYS);
        return coachScheduleMapper.findAllByCoachIdAndPossibleDateTime(coachId, startDateTime, endDateTime);
    }

    public void updateMatchYn(long id, boolean matchYn) {
        coachScheduleMapper.setMatchYn(id, matchYn);
    }
}
