package cobook.buddywisdom.feed.service;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cobook.buddywisdom.feed.domain.Feed;
import cobook.buddywisdom.feed.dto.FeedResponseDto;
import cobook.buddywisdom.feed.exception.AlreadyCheckedFeedException;
import cobook.buddywisdom.feed.exception.NotFoundFeedException;
import cobook.buddywisdom.feed.mapper.FeedMapper;
import cobook.buddywisdom.member.domain.Member;
import cobook.buddywisdom.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

	@Mock
	FeedMapper feedMapper;

	@Mock
	MemberService memberService;

	@InjectMocks
	FeedService feedService;

	private static Feed feed;
	private static Member member;

	@BeforeEach
	void setUp() {
		feed = Feed.of(1L,4L, 3L, "피드 메시지",
			false, LocalDateTime.now(), LocalDateTime.now());
		member = Member.builder().id(1L).name("이름").nickname("닉네임").build();
	}

	@Nested
	@DisplayName("피드 조회")
	class FeedTest {
		@Test
		@DisplayName("해당하는 피드 정보가 존재하면 피드 리스트를 반환한다.")
		void when_feedExistsWithInformation_expect_returnResponseList() {
			BDDMockito.given(feedMapper.findAllByReceiverId(anyLong()))
				.willReturn(List.of(feed));
			BDDMockito.given(memberService.getMember(anyLong()))
				.willReturn(member);

			List<FeedResponseDto> expectedResponse =
				feedService.getAllFeedByReceiverId(3L);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(1, expectedResponse.size());

			FeedResponseDto feedResponseDto = expectedResponse.get(0);
			Assertions.assertEquals(feed.getSenderId(), feedResponseDto.senderId());
			Assertions.assertEquals(feed.getReceiverId(), feedResponseDto.receiverId());
		}

		@Test
		@DisplayName("해당하는 피드 정보가 존재하지 않으면 빈 리스트를 반환한다.")
		void when_feedDoesNotExistsWithInformation_expect_returnEmptyList() {
			long nonExistentId = 100L;

			BDDMockito.given(feedMapper.findAllByReceiverId(anyLong()))
				.willReturn(Collections.emptyList());

			List<FeedResponseDto> expectedResponse =
				feedService.getAllFeedByReceiverId(nonExistentId);

			Assertions.assertTrue(expectedResponse.isEmpty());
		}

		@Test
		@DisplayName("해당하는 멤버 정보가 존재하면 템플릿 메시지에 이름과 닉네임 정보를 치환하여 반환한다.")
		void when_memberExistsWithInformation_expect_returnCombinedName() {
			BDDMockito.given(memberService.getMember(anyLong()))
				.willReturn(member);

			String convertedMessage =
				feedService.getConvertedMessage("%s", 1L);

			Assertions.assertEquals("이름(닉네임)", convertedMessage);
		}

		@Test
		@DisplayName("해당하는 멤버 정보의 닉네임이 없다면 하이픈으로 치환하 반환한다.")
		void when_memberHasNotNickname_expect_returnCombinedNameWithHyphen() {
			Member memberWithoutNickname = Member.builder().id(1L).name("이름").build();

			BDDMockito.given(memberService.getMember(anyLong()))
				.willReturn(memberWithoutNickname);

			String convertedMessage =
				feedService.getConvertedMessage("%s", 1L);

			Assertions.assertEquals("이름(-)", convertedMessage);
		}

		@Test
		@DisplayName("해당하는 멤버 정보가 존재하지 않으면 NotFoundMemberException을 반환한.")
		void when_memberDoesNotExists_expect_returnCombinedNameWithHyphen() {
			BDDMockito.given(memberService.getMember(anyLong()))
				.willThrow(RuntimeException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					feedService.getConvertedMessage("%s", 4L))
				.isInstanceOf(RuntimeException.class);
		}
	}

	@Nested
	@DisplayName("피드 생성")
	class CreateFeedTest {
		@Test
		@DisplayName("유효한 정보가 전달되면 피드 정보를 생성한다.")
		void when_InformationIsValid_expect_saveFeed() {
			BDDMockito.willDoNothing().given(feedMapper).save(any(Feed.class));

			feedService.saveFeed(4L, 3L, "메시지");

			BDDMockito.verify(feedMapper).save(any(Feed.class));
		}
	}

	@Nested
	@DisplayName("피드 확인")
	class UpdateFeedTest {
		@Test
		@DisplayName("해당하는 피드 정보가 존재하면 확인 상태값을 변경한다.")
		void when_feedExistsWithInformation_expect_updateUncheckedFeed() {
			BDDMockito.given(feedMapper.findByIdAndReceiverId(anyLong(), anyLong()))
				.willReturn(Optional.of(feed));
			BDDMockito.willDoNothing()
				.given(feedMapper).updateCheckYnById(anyLong());

			feedService.updateUncheckedFeed(4L, 1L);

			BDDMockito.verify(feedMapper).findByIdAndReceiverId(anyLong(), anyLong());
			BDDMockito.verify(feedMapper).updateCheckYnById(anyLong());
		}

		@Test
		@DisplayName("해당하는 피드 정보가 존재하지 않으면 NotFoundFeedException을 반환한다.")
		void when_feedDoesNotExists_expect_throwsNotFoundFeedException() {
			BDDMockito.given(feedMapper.findByIdAndReceiverId(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			AssertionsForClassTypes.assertThatThrownBy(() ->
					feedService.updateUncheckedFeed(4L, 1L))
				.isInstanceOf(NotFoundFeedException.class);
		}

		@Test
		@DisplayName("해당하는 피드 정보가 존재하지 않으면 AlreadyCheckedFeedException을 반환한다.")
		void when_feedAlreadyChecked_expect_throwsAlreadyCheckedFeedException() {
			Feed checkedFeed = Feed.of(1L,4L, 3L, "피드 메시지",
				true, LocalDateTime.now(), LocalDateTime.now());

			BDDMockito.given(feedMapper.findByIdAndReceiverId(anyLong(), anyLong()))
				.willReturn(Optional.of(checkedFeed));

			AssertionsForClassTypes.assertThatThrownBy(() ->
					feedService.updateUncheckedFeed(3L, 1L))
				.isInstanceOf(AlreadyCheckedFeedException.class);
		}

		@Test
		@DisplayName("해당하는 피드 정보가 존재하면 전체 피드의 확인 상태값을 변경한다.")
		void when_feedExistsWithInformation_expect_updateAllUncheckedFeed() {
			BDDMockito.given(feedMapper.existsByReceiverIdAndCheckYn(anyLong(), anyBoolean()))
				.willReturn(true);
			BDDMockito.willDoNothing()
				.given(feedMapper).updateCheckYnByReceiverIdAndCheckYn(anyLong());

			feedService.updateAllUncheckedFeed(4L);

			Assertions.assertTrue(feedMapper.existsByReceiverIdAndCheckYn(4L, false));
			BDDMockito.verify(feedMapper).updateCheckYnByReceiverIdAndCheckYn(anyLong());
		}

		@Test
		@DisplayName("이미 모든 피드가 확인되었으면 AlreadyCheckedFeedException를 반환한다.")
		void when_uncheckedFeedDoesNotExists_expect_throwAlreadyCheckedFeedException() {
			BDDMockito.given(feedMapper.existsByReceiverIdAndCheckYn(anyLong(), anyBoolean()))
				.willReturn(false);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					feedService.updateAllUncheckedFeed(1L))
				.isInstanceOf(AlreadyCheckedFeedException.class);
		}
	}
}
