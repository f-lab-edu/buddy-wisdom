package cobook.buddywisdom.coach.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachMapper {
    Integer deleteCoach(final long coachId, int active_yn);

}
