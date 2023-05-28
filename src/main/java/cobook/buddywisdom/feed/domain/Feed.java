package cobook.buddywisdom.feed.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {
	private Long id;
	private Long senderId;
	private Long receiverId;
	private String feedMessage;
	private boolean checkYn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static Feed of(Long id, Long senderId, Long receiverId, String feedMessage,
							boolean checkYn, LocalDateTime createdAt, LocalDateTime updatedAt) {
		Feed feed = new Feed();
		feed.id = id;
		feed.senderId = senderId;
		feed.receiverId = receiverId;
		feed.feedMessage = feedMessage;
		feed.checkYn = checkYn;
		feed.createdAt = createdAt;
		feed.updatedAt = updatedAt;
		return feed;
	}

	public static Feed requestOf(Long senderId, Long receiverId, String feedMessage) {
		Feed feed = new Feed();
		feed.senderId = senderId;
		feed.receiverId = receiverId;
		feed.feedMessage = feedMessage;
		return feed;
	}
}
