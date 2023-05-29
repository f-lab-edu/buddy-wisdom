package cobook.buddywisdom.member.mapper;

import cobook.buddywisdom.member.dto.CreateMemberDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberMapper {
    Integer createMember(CreateMemberDto dto);
}
