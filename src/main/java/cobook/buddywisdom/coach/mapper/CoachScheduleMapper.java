package cobook.buddywisdom.coach.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.coach.domain.CoachSchedule;

@Mapper
public interface CoachScheduleMapper {
	Optional<CoachSchedule> findByIdAndMatchYn(Long id, boolean matchYn);
	List<CoachSchedule> findAllByCoachId(Long coachId);
	void setMatchYn(Long id, boolean matchYn);
}
