package cobook.buddywisdom.mentee.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequest;
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
	public ResponseEntity<Optional<List<MenteeMonthlyScheduleResponse>>> getMenteeMonthlySchedule(@AuthenticationPrincipal CustomUserDetails member,
																								@RequestBody @Valid MenteeMonthlyScheduleRequest request) {
		return ResponseEntity.ok(Optional.ofNullable(menteeScheduleService.getMenteeMonthlySchedule(member.getId(), request)));
	}

	@GetMapping(value = "/schedule/feedback/{scheduleId}")
	public ResponseEntity<MenteeScheduleFeedbackResponse> getMenteeScheduleFeedback(@AuthenticationPrincipal CustomUserDetails member,
																					@PathVariable Long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeScheduleFeedback(member.getId(), scheduleId));
	}

}
