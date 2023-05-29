package cobook.buddywisdom.coach.controller;

import cobook.buddywisdom.coach.service.CoachService;
import cobook.buddywisdom.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coaches")
public class CoachController {


    // TODO : 추후에 로그인 히스토리 추가하여 휴면 회원 활성화 기능 추가 예정 (로그인 히스토리 테이블 필요)
    // 현재는 활성화 1, 탈퇴 0 으로 우선 진행
    private final CoachService coachService;

    // 코치 회원탈퇴
    @PutMapping
    public ResponseEntity<Integer> deleteCoachMember(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(coachService.deleteCoach(member.getId()));
    }
}
