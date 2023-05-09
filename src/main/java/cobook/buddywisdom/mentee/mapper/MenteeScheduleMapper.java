package cobook.buddywisdom.mentee.mapper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;

@Mapper
public interface MenteeScheduleMapper {
	MenteeMonthlySchedule findByMenteeIdAndPossibleDateTime(Long menteeId, LocalDateTime startDateTime, LocalDateTime endDateTime);
	Optional<MenteeScheduleFeedback> findByMenteeIdAndCoachingScheduleId(Long menteeId, Long coachingScheduleId);
	Optional<MenteeSchedule> findByMenteeIdAndCoachingScheduleIdAndCancelYn(Long menteeId, Long coachingScheduleId, boolean cancelYn);
	void save(MenteeSchedule menteeSchedule);
}
