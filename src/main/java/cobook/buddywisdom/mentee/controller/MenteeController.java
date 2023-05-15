package cobook.buddywisdom.mentee.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.global.security.CustomUserDetails;
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

	public MenteeController(MenteeScheduleService menteeScheduleService) {
		this.menteeScheduleService = menteeScheduleService;
	}

	@GetMapping(value = "/schedule")
	public ResponseEntity<List<MenteeMonthlyScheduleResponseDto>> getMenteeMonthlySchedule(@AuthenticationPrincipal CustomUserDetails member,
																								@RequestBody @Valid MenteeMonthlyScheduleRequestDto request) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeMonthlySchedule(member.getId(), request));
	}

	@GetMapping(value = "/schedule/feedback/{scheduleId}")
	public ResponseEntity<MenteeScheduleFeedbackResponseDto> getMenteeScheduleFeedback(@AuthenticationPrincipal CustomUserDetails member,
																					@PathVariable Long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeScheduleFeedback(member.getId(), scheduleId));
	}

	@GetMapping(value = "/schedule/create")
	public ResponseEntity<List<MyCoachScheduleResponseDto>> getMyCoachSchedule(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.ok(menteeScheduleService.getMyCoachSchedule(member.getId()));
	}

	@PostMapping(value = "/schedule/create/{scheduleId}")
	public ResponseEntity<MenteeScheduleResponseDto> createMenteeSchedule(@AuthenticationPrincipal CustomUserDetails member,
																			@PathVariable Long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.saveMenteeSchedule(member.getId(), scheduleId));
	}
}
