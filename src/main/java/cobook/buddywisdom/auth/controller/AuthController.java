package cobook.buddywisdom.auth.controller;

import cobook.buddywisdom.auth.dto.LoginRequestDto;
import cobook.buddywisdom.auth.service.AuthService;
import cobook.buddywisdom.global.jwt.TokenDto;
import cobook.buddywisdom.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/public/auth/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginRequestDto requestDto) {

        TokenDto token = authService.login(requestDto.getEmail(), requestDto.getPassword());

        return ResponseEntity.ok().body(token);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {

        // redis 에서 token 을 삭제
        authService.logout(userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}