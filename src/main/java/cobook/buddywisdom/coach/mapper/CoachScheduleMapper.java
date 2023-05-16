package cobook.buddywisdom.coach.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.coach.domain.CoachSchedule;

@Mapper
public interface CoachScheduleMapper {
	Optional<CoachSchedule> findByIdAndMatchYn(long id, boolean matchYn);
	List<CoachSchedule> findAllByCoachIdAndPossibleDateTime(long coachId, LocalDate startDateTime, LocalDate endDateTime);
	void setMatchYn(long id, boolean matchYn);
}
