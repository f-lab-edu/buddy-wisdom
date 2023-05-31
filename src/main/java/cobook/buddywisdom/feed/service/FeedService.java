package cobook.buddywisdom.feed.service;

import static cobook.buddywisdom.global.vo.MessageTemplate.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cobook.buddywisdom.feed.domain.Feed;
import cobook.buddywisdom.feed.dto.FeedResponseDto;
import cobook.buddywisdom.feed.exception.AlreadyCheckedFeedException;
import cobook.buddywisdom.feed.exception.NotFoundFeedException;
import cobook.buddywisdom.feed.mapper.FeedMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.member.domain.Member;
import cobook.buddywisdom.member.service.MemberService;

@Service
@Transactional
public class FeedService {

	private final FeedMapper feedMapper;
	private final MemberService memberService;

	private static final String EMPTY_MARK = "-";

	public FeedService(FeedMapper feedMapper, MemberService memberService) {
		this.feedMapper = feedMapper;
		this.memberService = memberService;
	}

	@Transactional(readOnly = true)
	public List<FeedResponseDto> getAllFeedByReceiverId(long memberId) {
		List<Feed> feedList = feedMapper.findAllByReceiverId(memberId);

		if (feedList.isEmpty()) {
			return Collections.emptyList();
		}

		feedList.forEach(this::updateFeedMessage);

		return feedList.stream()
			.map(FeedResponseDto::from)
			.toList();
	}

	private void updateFeedMessage(Feed feed) {
		String convertedMessage = getConvertedMessage(feed.getFeedMessage(), feed.getSenderId());
		feed.setFeedMessage(convertedMessage);
	}

	public String getConvertedMessage(String message, long senderId) {
		Member member = memberService.getMember(senderId);

		String nickname = member.getNickname();

		if (!StringUtils.hasText(member.getNickname())) {
			nickname = EMPTY_MARK;
		}

		String combinedName =
			MessageFormat.format(COMBINED_NAME.getTemplate(), member.getName(), nickname);

		return String.format(message, combinedName);
	}

	public void saveFeed(long senderId, long receiverId, String message) {
		Feed feed = Feed.requestOf(senderId, receiverId, message);
		feedMapper.save(feed);
	}

	public void updateUncheckedFeed(long memberId, long id) {
		Feed feed = feedMapper.findByIdAndReceiverId(id, memberId)
			.orElseThrow(() -> new NotFoundFeedException(ErrorMessage.NOT_FOUND_FEED));

		if (feed.isCheckYn()) {
			throw new AlreadyCheckedFeedException(ErrorMessage.ALREADY_CHECKED_FEED);
		}

		feedMapper.updateCheckYnById(id);
	}

	public void updateAllUncheckedFeed(long memberId) {
		if (!existsFeed(memberId, false)) {
			throw new AlreadyCheckedFeedException(ErrorMessage.ALREADY_CHECKED_FEED);
		}
		feedMapper.updateCheckYnByReceiverIdAndCheckYn(memberId);
	}

	public boolean existsFeed(long memberId, boolean checkYn) {
		return feedMapper.existsByReceiverIdAndCheckYn(memberId, checkYn);
	}
}
