package cobook.buddywisdom.member.controller;

import cobook.buddywisdom.global.domain.Response;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    // TODO : 회원가입 구현 후, 비밀번호 암호화
    // 회원 Detail 정보 조회
    @GetMapping("/detail")
    public ResponseEntity<Response> getMemberDetail() {
        return ResponseEntity.ok(
                Response.of(
                        "Find Member Success"
                        , memberService.getMemberDetail(getPrincipal())
        ));
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
