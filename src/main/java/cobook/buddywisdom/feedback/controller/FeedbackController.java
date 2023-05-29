package cobook.buddywisdom.feedback.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.dto.UpdateFeedbackRequestDto;
import cobook.buddywisdom.feedback.service.FeedbackService;
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
	public ResponseEntity<Void> updateFeedback(@PathVariable final MemberApiType memberApiType,
												@RequestBody @Valid final UpdateFeedbackRequestDto request) {
		feedbackService.updateFeedback(request.menteeScheduleId(), request.feedback(), memberApiType);
		return ResponseEntity.ok().build();
	}
}
