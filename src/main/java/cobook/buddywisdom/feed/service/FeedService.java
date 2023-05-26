package cobook.buddywisdom.feed.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.feed.domain.Feed;
import cobook.buddywisdom.feed.mapper.FeedMapper;

@Service
public class FeedService {

	private final FeedMapper feedMapper;

	public FeedService(FeedMapper feedMapper) {
		this.feedMapper = feedMapper;
	}

	@Transactional
	public void saveFeed(long senderId, long receiverId, String message) {
		feedMapper.save(Feed.of(senderId, receiverId, message));
	}
}
