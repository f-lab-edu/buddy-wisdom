package cobook.buddywisdom.mentee.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenteeMapper {
    Integer deleteMentee(final long menteeId, int active_yn);
}
