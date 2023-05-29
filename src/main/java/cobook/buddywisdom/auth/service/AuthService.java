package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.exception.InvalidCredentialsException;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.member.mapper.MemberMapper;
import cobook.buddywisdom.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberMapper memberMapper;
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
//    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public TokenDto login(final String email, final String password) {

        AuthMember user = memberMapper.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));

//         == 비밀번호 암호화 로직 들어갈 부분 ==
//         String encryptPassword = passwordEncoder.encode(password);
//         ==

        if(!password.equals(user.getPassword())) {
            throw new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS_EXCEPTION);
        }

        CustomUserDetails userDetails = CustomUserDetails.of(user);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userDetails, password);

        Authentication authenticate = managerBuilder.getObject().authenticate(token);
        // 스레드가 실행되는 동안 인증 상태를 유지하도록 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return tokenProvider.createToken(authenticate);
    }
}
