package cobook.buddywisdom.cancellation.controller;


import static cobook.buddywisdom.global.vo.MemberApiType.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.cancellation.dto.request.CancelRequestDto;
import cobook.buddywisdom.cancellation.dto.request.ConfirmCancelRequestDto;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.service.CancelRequestService;
import cobook.buddywisdom.cancellation.vo.DirectionType;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.global.vo.MemberApiType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CancelRequestController {

	private final CancelRequestService cancelRequestService;

	@GetMapping(value = "/{memberApiType}/cancel-request")
	public ResponseEntity<List<CancelRequestResponseDto>> getCancelRequestByDirection(@AuthenticationPrincipal CustomUserDetails member,
																						@PathVariable final MemberApiType memberApiType,
																						@RequestParam("direction") final DirectionType direction) {
		return ResponseEntity.ok(cancelRequestService.getCancelRequest(member.getId(), direction));
	}

	@PostMapping(value = "/{memberApiType}/cancel-request")
	public ResponseEntity<CancelRequestResponseDto> createCancelRequest(@AuthenticationPrincipal CustomUserDetails member,
																		@PathVariable final MemberApiType memberApiType,
																		@RequestBody @Valid final CancelRequestDto request) {
		CancelRequestResponseDto cancelRequestResponseDto = null;

		if (MENTEES.equals(memberApiType)) {
			cancelRequestResponseDto =
				cancelRequestService.saveCancelRequestByMentee(member.getId(), request.menteeScheduleId(), request.reason());
		}

		return ResponseEntity.ok(cancelRequestResponseDto);
	}

	@PatchMapping(value = "/{memberApiType}/cancel-request")
	public ResponseEntity<Void> updateCancelRequest(@AuthenticationPrincipal CustomUserDetails member,
													@PathVariable final MemberApiType memberApiType,
													@RequestBody @Valid final ConfirmCancelRequestDto request) {

		if (MENTEES.equals(memberApiType)) {
			cancelRequestService.confirmCancelRequestByMentee(member.getId(), request.id(), request.menteeScheduleId());
		}

		return ResponseEntity.ok().build();
	}
}
