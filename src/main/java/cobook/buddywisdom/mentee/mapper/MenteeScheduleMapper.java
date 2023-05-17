package cobook.buddywisdom.mentee.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;

@Mapper
public interface MenteeScheduleMapper {
	List<MenteeMonthlySchedule> findAllByMenteeIdAndPossibleDateTime(long menteeId, LocalDateTime startDateTime, LocalDateTime endDateTime);
	Optional<MenteeScheduleFeedback> findByMenteeIdAndCoachingScheduleId(long menteeId, long coachingScheduleId);
	Optional<MenteeSchedule> findByCoachingScheduleId(long coachingScheduleId);
	void save(MenteeSchedule menteeSchedule);
	void updateCoachingScheduleId(long currentCoachingId, long newCoachingId);
}
