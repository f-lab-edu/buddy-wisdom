package cobook.buddywisdom.member.service;

import cobook.buddywisdom.global.security.domain.vo.RoleType;
import cobook.buddywisdom.member.dto.CreateMemberDto;
import cobook.buddywisdom.member.exception.AdminDeletionNotAllowedException;
import cobook.buddywisdom.member.exception.DuplicatedMemberEmailException;
import cobook.buddywisdom.member.exception.FailedCreateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cobook.buddywisdom.member.domain.Member;
import cobook.buddywisdom.member.mapper.MemberMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.member.exception.NotFoundMemberException;


@Service
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public Member getMember(long id) {
		return memberMapper.findById(id)
			.orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));
	}

	public Integer createMember(CreateMemberDto dto) {

		// 이메일 중복 체크
		memberMapper.findByEmail(dto.getEmail()).ifPresent(member -> {
			throw new DuplicatedMemberEmailException(ErrorMessage.DUPLICATED_MEMBER_EMAIL_EXCEPTION);
		});

		// 가입 수행
		Integer insertCount = memberMapper.createMember(CreateMemberDto.of(dto, passwordEncoder));

		// 가입 실패 시 에러 수행
		if(insertCount < 1) {
			throw new FailedCreateMemberException(ErrorMessage.FAILED_CREATE_MEMBER_EXCEPTION);
		}
		return insertCount;
	}

	public void deleteMember(final long memberId) throws AdminDeletionNotAllowedException {

		// 회원 존재 여부 체크
		Member member = memberMapper.findById(memberId).orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));

		// 관리자 권한 체크
		if(member.getRole().equals(RoleType.ADMIN)) {
			throw new AdminDeletionNotAllowedException(ErrorMessage.ADMIN_DELETION_NOT_ALLOWED_EXCEPTION);
		} else {
			memberMapper.deleteMember(memberId);
		}

	}
}
