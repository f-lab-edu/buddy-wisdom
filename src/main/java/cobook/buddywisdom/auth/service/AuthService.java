package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.global.domain.vo.AuthResponse;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.member.repository.MemberMapper;
import cobook.buddywisdom.mentee.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberMapper memberMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public TokenDto login(final String email, final String password) {

        AuthResponse user = memberMapper.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));

        CustomUserDetails customUserDetails = CustomUserDetails.of(user);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(customUserDetails, password);

        Authentication authenticate = authenticationManager.authenticate(token);
        // 스레드가 실행되는 동안 인증 상태를 유지하도록 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return tokenProvider.createToken(authenticate);
    }
}
