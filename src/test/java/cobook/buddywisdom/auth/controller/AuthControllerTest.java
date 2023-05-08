//package cobook.buddywisdom.auth.controller;
//
//import cobook.buddywisdom.auth.WithMockCustomUser;
//import cobook.buddywisdom.auth.dto.LoginRequestDto;
//import cobook.buddywisdom.auth.mapper.MemberMapper;
//import cobook.buddywisdom.global.security.domain.vo.AuthMember;
//import cobook.buddywisdom.global.jwt.TokenDto;
//import cobook.buddywisdom.global.jwt.TokenProvider;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.BDDMockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TokenProvider tokenProvider;
//    @MockBean
//    private MemberMapper memberMapper;
//
//
//
//    @Test
//    @DisplayName("[성공] 가상 객체를 생성하여 유저를 조회하고 토큰을 생성하여 반환한다.")
//    void when_createToken_expect_returnToken() throws Exception {
//
//        // 가상의 사용자 객체 생성
//        String email = "admin@mock.test";
//        String password = "password";
//        AuthMember member = new AuthMember(1, email, password, "ADMIN");
//
//        // 가상의 토큰 생성
//        TokenDto expectedToken = TokenDto.of("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjMzODI3MjUwLCJleHAiOjE2MzM4MzA4NTB9.8k3u3f1dJ6U5e6WjFZf5sq56ylYnFzDJdJ1S8QjL_AY"
//                ,"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjMzODI3MjUwLCJleHAiOjE2MzM4MzA4NTB9.8k3u3f1dJ6U5e6WjFZf5sq56ylYnFzDJdJ1S8QjL_AY");
//
//        BDDMockito.given(memberMapper.findByEmailAndPassword(email, password)).willReturn(Optional.of(member));
//        BDDMockito.when(tokenProvider.createToken(any(Authentication.class))).thenReturn(expectedToken);
//
//        // 로그인 요청 및 토큰 확인
//        mockMvc.perform(post("/api/v1/public/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new LoginRequestDto(email, password)))
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.access_token").value(expectedToken.getAccessToken()))
//                .andExpect(jsonPath("$.refresh_token").value(expectedToken.getRefreshToken()))
//        ;
//    }
//
//}
