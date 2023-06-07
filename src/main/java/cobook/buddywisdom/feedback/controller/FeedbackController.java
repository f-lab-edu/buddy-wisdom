package cobook.buddywisdom.feedback.controller;

import static cobook.buddywisdom.global.vo.MemberApiType.*;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.dto.UpdateFeedbackRequestDto;
import cobook.buddywisdom.feedback.service.FeedbackService;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.global.vo.MemberApiType;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FeedbackController {

	private final FeedbackService feedbackService;

	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@GetMapping("/{memberApiType}/feedback/{scheduleId}")
	public ResponseEntity<Optional<FeedbackResponseDto>> getFeedBack(@PathVariable final MemberApiType memberApiType,
															@PathVariable final long scheduleId) {
		return ResponseEntity.ok(feedbackService.getFeedback(scheduleId));
	}

	@PatchMapping("/{memberApiType}/feedback")
	public ResponseEntity<Void> updateFeedback(@AuthenticationPrincipal CustomUserDetails member,
												@PathVariable final MemberApiType memberApiType,
												@RequestBody @Valid final UpdateFeedbackRequestDto request) {
		if (COACHES.equals(memberApiType)) {
			feedbackService.updateFeedbackByCoach(member.getId(), request.menteeScheduleId(), request.feedback());
		} else if (MENTEES.equals(memberApiType)) {
			feedbackService.updateFeedbackByMentee(member.getId(), request.menteeScheduleId(), request.feedback());
		}

		return ResponseEntity.noContent().build();
	}
}
