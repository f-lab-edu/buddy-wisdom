package cobook.buddywisdom.mentee.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;

@Mapper
public interface MenteeScheduleMapper {
	MenteeMonthlySchedule findByMenteeIdAndPossibleDateTime(Long menteeId, String date);
	Optional<MenteeScheduleFeedback> findByMenteeIdAndCoachingScheduleId(Long menteeId, Long coachingScheduleId);
}
