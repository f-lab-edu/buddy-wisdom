package cobook.buddywisdom.member.mapper;

import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.member.domain.Member;

import cobook.buddywisdom.member.dto.CreateMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;


@Mapper
public interface MemberMapper {
    Optional<AuthMember> findByEmail(final String username);
    Optional<Member> findById(long id);


    Integer createMember(CreateMemberDto dto);
    Integer deleteMember(long memberId);
}
