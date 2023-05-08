package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.auth.WithMockCustomUser;
import cobook.buddywisdom.auth.mapper.MemberMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.exception.NotFoundMemberException;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceTest {

    @MockBean
    private MemberMapper memberMapper;

    @Test
    @DisplayName("[성공] 이메일과 비밀번호를 사용하여 회원을 조회하고 member id 를 반환한다.")
    void when_memberExistWithInformation_expect_returnOK() {
        // 가상의 사용자 객체 생성
        String email = "admin@mock.test";
        String password = "password";
        AuthMember member = new AuthMember(1, email, password, "ADMIN");

        BDDMockito.given(memberMapper.findByEmailAndPassword(email, password)).willReturn(Optional.of(member));

        Assertions.assertNotNull(member);
        Assertions.assertEquals(1, member.getId());

    }

    @Test
    @DisplayName("[실패] 이메일과 비밀번호를 사용하여 회원을 조회하고 NULL 을 반환한다.")
    void when_memberExistWithInformation_expect_returnNull() {
        // 가상의 사용자 객체 생성
        String email = "admin@mock.test";
        String password = "password";
        AuthMember member = null;

        BDDMockito.given(memberMapper.findByEmailAndPassword(email, password))
                .willReturn(Optional.ofNullable(member));

        Assertions.assertNull(member);
    }
}
