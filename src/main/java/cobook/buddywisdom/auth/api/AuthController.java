package cobook.buddywisdom.auth.api;

import cobook.buddywisdom.auth.dto.LoginRequestDto;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    // TODO : 회원가입 구현 후, 로그인도 암호화된 정보로 비교하도록 수정
    @PostMapping("/public/auth/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginRequestDto requestDto) {

        TokenDto token = authService.login(requestDto.getEmail(), requestDto.getPassword());
        String refreshToken = token.getRefreshToken();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, getCookie(refreshToken).toString())
                .body(token);
    }

    // TODO : Redis 에 저장하여 가져오도록 수정
    private ResponseCookie getCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .secure(true)
                .path("/")
                .maxAge(18000)
                .build();
    }
}