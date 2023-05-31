package cobook.buddywisdom.feed.dto;

import java.time.LocalDateTime;

import cobook.buddywisdom.feed.domain.Feed;

public record FeedResponseDto (
	long id,
	long senderId,
	long receiverId,
	String feedMessage,
	boolean checkYn
) {
	public static FeedResponseDto from(Feed feed) {
		return new FeedResponseDto(
			feed.getId(),
			feed.getSenderId(),
			feed.getReceiverId(),
			feed.getFeedMessage(),
			feed.isCheckYn()
		);
	}
}
