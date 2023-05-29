package cobook.buddywisdom.feed.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.feed.domain.Feed;

@Mapper
public interface FeedMapper {
	List<Feed> findAllByReceiverId(long receiverId);
	Optional<Feed> findByIdAndReceiverId(long id, long receiverId);
	void save(Feed feed);
	void updateCheckYnById(long id, boolean checkYn);
	void updateCheckYnByReceiverIdAndCheckYn(long receiverId, boolean currentStatus, boolean newStatus);
	boolean existsByReceiverIdAndCheckYn(long receiverId, boolean checkYn);
}
