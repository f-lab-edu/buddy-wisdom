package cobook.buddywisdom.feed.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.feed.dto.FeedResponseDto;
import cobook.buddywisdom.feed.service.FeedService;
import cobook.buddywisdom.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {

	private final FeedService feedService;

	public FeedController(FeedService feedService) {
		this.feedService = feedService;
	}

	@GetMapping
	public ResponseEntity<List<FeedResponseDto>> getAllFeed(@AuthenticationPrincipal CustomUserDetails member) {
		return ResponseEntity.ok(feedService.getAllFeedByReceiverId(member.getId()));
	}

	@PatchMapping("/{feedId}")
	public ResponseEntity<Void> updateUncheckedFeed(@AuthenticationPrincipal CustomUserDetails member,
													@PathVariable final long feedId) {
		feedService.updateUncheckedFeed(member.getId(), feedId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping
	public ResponseEntity<Void> updateAllUncheckedFeed(@AuthenticationPrincipal CustomUserDetails member) {
		feedService.updateAllUncheckedFeed(member.getId());
		return ResponseEntity.noContent().build();
	}
}
