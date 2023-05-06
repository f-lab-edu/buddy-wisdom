package cobook.buddywisdom.member.repository;

import cobook.buddywisdom.global.domain.vo.AuthResponse;
import cobook.buddywisdom.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Mapper
@Repository
public interface MemberMapper {

    Optional<AuthResponse> findByEmailAndPassword(final String email, final String password);
    Optional<AuthResponse> findByEmail(final String username);
    Optional<Member> findByMemberId(final int memberId);
}
