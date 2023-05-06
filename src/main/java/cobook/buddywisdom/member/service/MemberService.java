package cobook.buddywisdom.member.service;

import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.member.domain.Member;
import cobook.buddywisdom.member.repository.MemberMapper;
import cobook.buddywisdom.mentee.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public Member getMemberDetail(final CustomUserDetails user) {
        return memberMapper.findByMemberId(user.getId())
                .orElseThrow(()
                        -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));
    }
}
