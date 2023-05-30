package cobook.buddywisdom.feedback.controller;

import static org.mockito.ArgumentMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cobook.buddywisdom.feedback.domain.Feedback;
import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.dto.UpdateFeedbackRequestDto;
import cobook.buddywisdom.feedback.service.FeedbackService;
import cobook.buddywisdom.global.vo.MemberApiType;
import cobook.buddywisdom.util.WithMockCustomUser;

@AutoConfigureMybatis
@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTest {

	@MockBean
	private FeedbackService feedbackService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String BASE_URL = "/api/v1/";

	@Nested
	@WithMockCustomUser
	@DisplayName("피드백 조회")
	class FeedbackTest {
		@ParameterizedTest
		@ValueSource(strings = {"coaches", "mentees"})
		@DisplayName("유효한 스케줄 id를 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdIsValid_expect_callMethodAndReturn200Ok(String memberApiType) throws Exception {
			Long scheduleId = 1L;

			Feedback feedback = Feedback.of(scheduleId, "코치 피드백", "멘티 피드백");
			FeedbackResponseDto feedbackResponseDto = FeedbackResponseDto.from(feedback);

			BDDMockito.given(feedbackService.getFeedback(anyLong()))
				.willReturn(Optional.of(feedbackResponseDto));

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_URL + memberApiType + "/feedback/" + scheduleId))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(feedbackService).getFeedback(anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}

	@Nested
	@WithMockCustomUser
	@DisplayName("피드백 확인")
	class UpdateFeedbackTest {
		@ParameterizedTest
		@ValueSource(strings = {"coaches", "mentees"})
		@DisplayName("유효한 피드백 정보가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_requestInformationIsValid_expect_callMethodAndReturn200Ok(String memberApiType) throws Exception {
			UpdateFeedbackRequestDto request = new UpdateFeedbackRequestDto(1L, "피드백");

			BDDMockito.willDoNothing()
				.given(feedbackService).updateFeedback(anyLong(), anyString(), any(MemberApiType.class));

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.patch(BASE_URL + memberApiType + "/feedback")
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(feedbackService).updateFeedback(anyLong(), anyString(), any(MemberApiType.class));
			response.andExpect(MockMvcResultMatchers.status().isNoContent());
		}
	}
}
