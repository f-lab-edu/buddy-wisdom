package cobook.buddywisdom.mentee.controller;

import java.util.List;

import cobook.buddywisdom.mentee.service.MenteeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.mentee.dto.request.UpdateMenteeScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeMonthlyScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleFeedbackResponseDto;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MyCoachScheduleResponseDto;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/mentees")
public class MenteeController {

	private final MenteeScheduleService menteeScheduleService;
	private final MenteeService menteeService;

	public MenteeController(MenteeScheduleService menteeScheduleService, MenteeService menteeService) {
		this.menteeScheduleService = menteeScheduleService;
		this.menteeService = menteeService;
	}


	@GetMapping(value = "/schedule/monthly")
	public ResponseEntity<List<MenteeMonthlyScheduleResponseDto>> getMenteeMonthlySchedule(@AuthenticationPrincipal CustomUserDetails member,
																								@RequestBody @Valid MenteeMonthlyScheduleRequestDto request) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeMonthlySchedule(member.getId(), request));
	}

	@GetMapping(value = "/schedule/{scheduleId}")
	public ResponseEntity<MenteeScheduleFeedbackResponseDto> getMenteeScheduleFeedback(@AuthenticationPrincipal CustomUserDetails member,
																					@PathVariable final long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeScheduleFeedback(member.getId(), scheduleId));
	}

	@GetMapping(value = "/schedule")
	public ResponseEntity<List<MyCoachScheduleResponseDto>> getMyCoachSchedule(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.ok(menteeScheduleService.getMyCoachSchedule(member.getId()));
	}

	@PostMapping(value = "/schedule/{scheduleId}")
	public ResponseEntity<MenteeScheduleResponseDto> createMenteeSchedule(@AuthenticationPrincipal CustomUserDetails member,
																			@PathVariable final long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.saveMenteeSchedule(member.getId(), scheduleId));
	}

	@PatchMapping(value = "/schedule")
	public ResponseEntity<Void> update(@AuthenticationPrincipal CustomUserDetails member,
										@RequestBody @Valid UpdateMenteeScheduleRequestDto request) {
		menteeScheduleService.updateMenteeSchedule(member.getId(), request.currentCoachingId(), request.newCoachingId());
		return ResponseEntity.ok().build();
	}

	// 멘티 회원탈퇴
	@PutMapping
	public ResponseEntity<Integer> deleteCoachMember(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(menteeService.deleteMentee(member.getId()));
	}
}
