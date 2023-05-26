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
	private LocalDateTime createdAt;

	public static Feed of(Long senderId, Long receiverId, String feedMesage) {
		Feed feed = new Feed();
		feed.senderId = senderId;
		feed.receiverId = receiverId;
		feed.feedMessage = feedMesage;
		return feed;
	}
}
