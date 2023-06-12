package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.exception.InvalidCredentialsException;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.redis.RedisService;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.member.mapper.MemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final MemberMapper memberMapper;

    @Transactional
    public TokenDto login(final String email, final String password) {

        // 회원이 존재하는지 체크
        AuthMember userCheck = memberMapper.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS_EXCEPTION));

        // 암호화된 비밀번호 매칭
        if(!passwordEncoder.matches(password, userCheck.getPassword())) {
            throw new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS_EXCEPTION);
        }

        CustomUserDetails userDetails = CustomUserDetails.of(userCheck);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userDetails, password);

        Authentication authenticate = managerBuilder.getObject().authenticate(token);
        // 스레드가 실행되는 동안 인증 상태를 유지하도록 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // redis 조회
        if(redisService.isExists(userCheck.getId())) {
            return new TokenDto(redisService.getValues(userCheck.getId()), redisService.getValues(userCheck.getId()));
        } else {
            return tokenProvider.createToken(authenticate);
        }
    }

    public void logout(long memberId) {
        log.debug("Redis Delete Get : " + redisService.getValues(memberId));
        redisService.deleteValues(memberId);
    }

}
