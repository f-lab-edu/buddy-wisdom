package cobook.buddywisdom.auth.service;

import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.exception.InvalidCredentialsException;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.auth.mapper.AuthMapper;
import cobook.buddywisdom.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto login(final String email, final String password) throws NoSuchAlgorithmException {

        // 비밀번호 암호화
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        // 해시된 바이트 배열을 16진수 문자열로 변환하는 과정
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String encryptPassword = hexString.toString();

        // 회원이 존재하는지 체크
        authMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));

        // 회원이 맞는지 체크
        AuthMember userCheck = authMapper.findByEmailAndPassword(email, encryptPassword)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS_EXCEPTION));

        CustomUserDetails userDetails = CustomUserDetails.of(userCheck);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userDetails, encryptPassword);

        Authentication authenticate = managerBuilder.getObject().authenticate(token);
        // 스레드가 실행되는 동안 인증 상태를 유지하도록 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return tokenProvider.createToken(authenticate);
    }

}
