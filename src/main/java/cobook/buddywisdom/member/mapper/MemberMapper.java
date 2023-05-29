package cobook.buddywisdom.member.mapper;

import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.member.domain.Member;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;


@Mapper
public interface MemberMapper {

    Optional<AuthMember> findByEmailAndPassword(final String email, final String password);
    Optional<AuthMember> findByEmail(final String username);
    Optional<Member> findById(long id);
}