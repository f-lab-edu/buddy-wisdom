package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.auth.WithMockCustomUser;
import cobook.buddywisdom.auth.dto.LoginRequestDto;
import cobook.buddywisdom.auth.mapper.MemberMapper;
import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private MemberMapper memberMapper;



    @Test
    @DisplayName("가상 객체를 생성하여 유저를 조회하고 토큰을 생성한다.")
    @WithMockCustomUser
    void token_generation_test() throws Exception {

        // 가상의 사용자 객체 생성
        String email = "admin@mock.test";
        String password = "password";
        AuthMember member = new AuthMember(1, email, password, "ADMIN");

        // 가상의 토큰 생성
        TokenDto fakeToken = TokenDto.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjMzODI3MjUwLCJleHAiOjE2MzM4MzA4NTB9.8k3u3f1dJ6U5e6WjFZf5sq56ylYnFzDJdJ1S8QjL_AY"
        ,"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjMzODI3MjUwLCJleHAiOjE2MzM4MzA4NTB9.8k3u3f1dJ6U5e6WjFZf5sq56ylYnFzDJdJ1S8QjL_AY");

        when(tokenProvider.createToken(any(Authentication.class))).thenReturn(fakeToken);

        when(memberMapper.findByEmailAndPassword(email, password)).thenReturn(Optional.of(member));

        // 로그인 요청 및 토큰 확인
        mockMvc.perform(post("/api/v1/public/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new LoginRequestDto(email, password)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(fakeToken.getAccessToken()))
                .andExpect(jsonPath("$.refresh_token").value(fakeToken.getRefreshToken()))
        ;
    }



}