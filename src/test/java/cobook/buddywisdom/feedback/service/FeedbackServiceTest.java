package cobook.buddywisdom.feedback.service;

import static org.mockito.ArgumentMatchers.*;

import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cobook.buddywisdom.feedback.domain.Feedback;
import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.exception.NotFoundFeedbackException;
import cobook.buddywisdom.feedback.mapper.FeedbackMapper;
import cobook.buddywisdom.global.vo.MemberApiType;
import cobook.buddywisdom.util.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

	@Mock
	FeedbackMapper feedbackMapper;

	@InjectMocks
	FeedbackService feedbackService;

	@Nested
	@WithMockCustomUser
	@DisplayName("피드백 조회")
	class FeedbackTest {
		@Test
		@DisplayName("해당하는 피드백이 존재하면 해당 정보를 반환한다.")
		void when_feedbackExists_expect_returnResponse() {
			long scheduleId = 1L;

			Feedback feedback = Feedback.of(scheduleId, "코치 피드백", "멘티 피드백");

			BDDMockito.given(feedbackMapper.findByMenteeScheduleId(anyLong()))
				.willReturn(Optional.of(feedback));

			Optional<FeedbackResponseDto> feedbackResponseDto =
				feedbackService.getFeedback(scheduleId);

			Assertions.assertNotNull(feedbackResponseDto);
		}

		@Test
		@DisplayName("해당하는 피드백이 존재하지 않으면 빈 객체를 반환한다.")
		void when_feedbackDoesNotExists_expect_returnOptionalEmpty() {
			long scheduleId = 1L;

			BDDMockito.given(feedbackMapper.findByMenteeScheduleId(anyLong()))
				.willReturn(Optional.empty());

			Optional<FeedbackResponseDto> expectedResponse =
				feedbackService.getFeedback(scheduleId);

			Assertions.assertTrue(expectedResponse.isEmpty());
		}
	}

	@Nested
	@WithMockCustomUser
	@DisplayName("피드백 확인")
	class UpdateFeedbackTest {
		@Test
		@DisplayName("피드백 내역이 존재하면 apiType이 mentees일 때 멘티 피드백을 추가한다.")
		void when_feedbackExistsAndApiTypeIsMentees_expect_updateFeedbackByApiType() {
			Feedback feedback = Feedback.of(1L, null, null);

			BDDMockito.given(feedbackMapper.findByMenteeScheduleId(anyLong()))
					.willReturn(Optional.of(feedback));
			BDDMockito.willDoNothing()
				.given(feedbackMapper).updateMenteeFeedbackByMenteeScheduleId(anyLong(), anyString());

			feedbackService.updateFeedback(1L, "멘티 피드백", MemberApiType.MENTEES);

			BDDMockito.verify(feedbackMapper)
				.updateMenteeFeedbackByMenteeScheduleId(1L, "멘티 피드백");
		}

		@Test
		@DisplayName("피드백 내역이 존재하면 apiType이 coaches일 때 멘티 피드백을 추가한다.")
		void when_feedbackExistsAndApiTypeIsCoaches_expect_updateFeedbackByApiType() {
			Feedback feedback = Feedback.of(1L, null, null);

			BDDMockito.given(feedbackMapper.findByMenteeScheduleId(anyLong()))
				.willReturn(Optional.of(feedback));
			BDDMockito.willDoNothing()
				.given(feedbackMapper).updateCoachFeedbackByMenteeScheduleId(anyLong(), anyString());

			feedbackService.updateFeedback(1L, "코치 피드백", MemberApiType.COACHES);

			BDDMockito.verify(feedbackMapper)
				.updateCoachFeedbackByMenteeScheduleId(1L, "코치 피드백");
		}

		@ParameterizedTest
		@ValueSource(strings = {"MENTEES", "COACHES"})
		@DisplayName("피드백 내역이 존재하지 않으면 NotFoundFeedbackException을 반환한다.")
		void when_feedbackDoesNotExists_expect_throwsNotFoundFeedbackException(String apiType) {
			BDDMockito.given(feedbackMapper.findByMenteeScheduleId(anyLong()))
				.willReturn(Optional.empty());

			AssertionsForClassTypes.assertThatThrownBy(() ->
				feedbackService.updateFeedback(1L, "피드백", MemberApiType.valueOf(apiType)))
			.isInstanceOf(NotFoundFeedbackException.class);
		}
	}
}
