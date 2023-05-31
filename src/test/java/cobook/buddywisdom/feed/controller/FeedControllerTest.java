package cobook.buddywisdom.feed.controller;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cobook.buddywisdom.feed.domain.Feed;
import cobook.buddywisdom.feed.dto.FeedResponseDto;
import cobook.buddywisdom.feed.service.FeedService;
import cobook.buddywisdom.util.WithMockCustomUser;

@AutoConfigureMybatis
@WebMvcTest(FeedController.class)
class FeedControllerTest {

	@MockBean
	private FeedService feedService;

	@Autowired
	private MockMvc mockMvc;

	private static final String BASE_URL = "/api/v1/feed";

	@Nested
	@DisplayName("피드 조회")
	class FeedTest {
		@Test
		@WithMockCustomUser
		@DisplayName("인증된 멤버의 피드 내역을 조회하기 위해 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMemberExists_expect_callMethodAndReturn200Ok() throws Exception {
			Feed feed = Feed.of(1L,4L, 3L, "피드 메시지",
				false, LocalDateTime.now(), LocalDateTime.now());
			FeedResponseDto feedResponseDto = FeedResponseDto.from(feed);

			BDDMockito.when(feedService.getAllFeedByReceiverId(anyLong()))
				.thenReturn(List.of(feedResponseDto));

			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(feedService).getAllFeedByReceiverId(anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}

	@Nested
	@WithMockCustomUser
	@DisplayName("피드 확인")
	class UpdateFeedTest {
		@Test
		@DisplayName("유효한 피드 id가 전달되면 메서드를 호출하고 200 Ok를 반환한다.")
		void when_feedIdIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			long feedId = 1;

			BDDMockito.willDoNothing()
				.given(feedService).updateUncheckedFeed(anyLong(), anyLong());

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.patch(BASE_URL + "/" + feedId)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(feedService).updateUncheckedFeed(anyLong(), anyLong());
			response.andExpect(MockMvcResultMatchers.status().isNoContent());
		}

		@Test
		@DisplayName("인증된 멤버 정보가 존재하면 메서드를 호출하고 200 Ok를 반환한다.")
		void when_authenticatedMemberExists_expect_callMethodAndReturn200Ok() throws Exception {
			BDDMockito.willDoNothing()
				.given(feedService).updateAllUncheckedFeed(anyLong());

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.patch(BASE_URL)
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(feedService).updateAllUncheckedFeed(anyLong());
			response.andExpect(MockMvcResultMatchers.status().isNoContent());
		}
	}
}
