package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.global.security.domain.vo.RoleType;
import cobook.buddywisdom.auth.mapper.MemberMapper;
import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @Test
    @DisplayName("[성공] 이메일과 비밀번호를 사용하여 회원을 조회하고 member id 를 반환한다.")
    void when_memberExistWithInformation_expect_return_success() {
        // 가상의 사용자 객체 생성
        String email = "admin@mock.test";
        String password = "password";
        String role = RoleType.ADMIN.name();
        AuthMember expectedMember = new AuthMember(1L, email, password, role);

        // given
        BDDMockito.given(memberMapper.findByEmailAndPassword(email, password))
                    .willReturn(Optional.of(expectedMember));

        // when
        Optional<AuthMember> actualUser = memberMapper.findByEmailAndPassword(email, password);

        // then
        assertTrue(actualUser.isPresent());
        assertEquals("회원 아이디 조회 성공", expectedMember.getId(), actualUser.get().getId());
        assertEquals("회원 이메일 조회 성공", expectedMember.getEmail(), actualUser.get().getEmail());
        assertEquals("회원 비밀번호 조회 성공", expectedMember.getPassword(), actualUser.get().getPassword());
        assertEquals("회원 역할 조회 성공", expectedMember.getRole(), actualUser.get().getRole());
    }

    @Test
    @DisplayName("[실패] 이메일과 비밀번호를 사용하여 회원을 조회하고 NULL 을 반환한다.")
    void when_memberExistWithInformation_expect_return_failed() {
        // 가상의 사용자 객체 생성
        String email = "admin@mock.test";
        String password = "password";

        // given
        BDDMockito.given(memberMapper.findByEmailAndPassword(email, password))
                .willReturn(Optional.empty());

        // when
        Optional<AuthMember> actualUser = memberMapper.findByEmailAndPassword(email, password);

        // then
        assertFalse(actualUser.isPresent());
    }
}
