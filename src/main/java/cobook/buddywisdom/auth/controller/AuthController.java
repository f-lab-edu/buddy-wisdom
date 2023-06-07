package cobook.buddywisdom.auth.controller;

import cobook.buddywisdom.auth.dto.LoginRequestDto;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    // TODO : 외부 서버 redis 커넥션 오류로 추후 리팩토링 시 다시 추가할 예정. 현재는 쿠키로 진행.
    private final AuthService authService;


    @PostMapping("/public/auth/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenDto token = authService.login(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, getCookie(token.getRefreshToken()).toString())
                .body(token);
    }


    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie myCookie = new Cookie("refreshToken", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);
        return ResponseEntity.noContent().build();
    }


    private ResponseCookie getCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .secure(true)
                .path("/")
                .maxAge(18000)
                .build();
    }
}