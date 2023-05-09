package cobook.buddywisdom.coach.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.coach.domain.CoachSchedule;

@Mapper
public interface CoachScheduleMapper {
	Optional<CoachSchedule> findById(Long id);
}
