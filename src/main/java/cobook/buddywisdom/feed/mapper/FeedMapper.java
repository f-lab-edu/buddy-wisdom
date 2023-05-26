package cobook.buddywisdom.feed.mapper;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.feed.domain.Feed;

@Mapper
public interface FeedMapper {
	void save(Feed feed);
}
