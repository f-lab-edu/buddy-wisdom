package cobook.buddywisdom.member.mapper;

import cobook.buddywisdom.coach.dto.CreateMemberDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberMapper {
    Integer createMember(CreateMemberDto dto);
}
