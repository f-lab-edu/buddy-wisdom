package cobook.buddywisdom.cancellation.controller;

import static cobook.buddywisdom.cancellation.controller.DirectionType.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.cancellation.dto.request.CancelRequestDto;
import cobook.buddywisdom.cancellation.dto.request.ConfirmCancelRequestDto;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.service.CancelRequestService;
import cobook.buddywisdom.global.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/mentees/schedule")
public class MenteeCancelRequestController {

	private final CancelRequestService cancelRequestService;

	public MenteeCancelRequestController(CancelRequestService cancelRequestService) {
		this.cancelRequestService = cancelRequestService;
	}

	@GetMapping(value = "/cancel-request/sent")
	public ResponseEntity<List<CancelRequestResponseDto>> getSentCancelRequest(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.ok(cancelRequestService.getCancelRequest(member.getId(), SENT));
	}

	@GetMapping(value = "/cancel-request/received")
	public ResponseEntity<List<CancelRequestResponseDto>> getReceivedCancelRequest(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.ok(cancelRequestService.getCancelRequest(member.getId(), RECEIVED));
	}

	@PostMapping(value = "/cancel-request")
	public ResponseEntity<CancelRequestResponseDto> createCancelRequest(@AuthenticationPrincipal CustomUserDetails member,
													@RequestBody @Valid CancelRequestDto request) {
		return ResponseEntity.ok(cancelRequestService.saveCancelRequestByMentee(member.getId(), request.menteeScheduleId(), request.reason()));
	}

	@PatchMapping(value = "/cancel-request")
	public ResponseEntity<Void> updateCancelRequest(@RequestBody @Valid ConfirmCancelRequestDto request) {
		cancelRequestService.confirmCancelRequest(request.id(), request.menteeScheduleId());
		return ResponseEntity.ok().build();
	}
}
