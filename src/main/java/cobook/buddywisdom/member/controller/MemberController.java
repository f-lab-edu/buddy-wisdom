package cobook.buddywisdom.member.controller;

import cobook.buddywisdom.member.dto.CreateMemberDto;
import cobook.buddywisdom.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    // TODO : 마지막 로그인으로부터 1년이 지난 회원은 비활성화 처리 (로그인 히스토리 필요)
    // 회원가입
    @PostMapping("/public/members")
    public ResponseEntity<Integer> createMember(@RequestBody CreateMemberDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(requestDto));
    }
}
