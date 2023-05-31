package cobook.buddywisdom.member.service;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.member.domain.Member;
import cobook.buddywisdom.member.mapper.MemberMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.member.exception.NotFoundMemberException;

@Service
public class MemberService {

	private final MemberMapper memberMapper;

	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	public Member getMember(long id) {
		return memberMapper.findById(id)
			.orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));
	}
}
