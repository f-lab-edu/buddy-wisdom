package cobook.buddywisdom.feedback.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.feedback.domain.Feedback;
import cobook.buddywisdom.feedback.dto.FeedbackResponseDto;
import cobook.buddywisdom.feedback.exception.NotFoundFeedbackException;
import cobook.buddywisdom.feedback.mapper.FeedbackMapper;
import cobook.buddywisdom.global.util.MessageUtil;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import cobook.buddywisdom.util.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

	@Mock
	FeedbackMapper feedbackMapper;

	@Mock
	MenteeScheduleService menteeScheduleService;

	@Mock
	CoachScheduleService coachScheduleService;

	@Mock
	FeedMessageProducer feedMessageProducer;

	@Mock
	MessageUtil messageUtil;

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

			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.of(feedback));

			Optional<FeedbackResponseDto> feedbackResponseDto = feedbackService.getFeedback(scheduleId);

			assertNotNull(feedbackResponseDto);
		}

		@Test
		@DisplayName("해당하는 피드백이 존재하지 않으면 빈 객체를 반환한다.")
		void when_feedbackDoesNotExists_expect_returnOptionalEmpty() {
			long scheduleId = 1L;

			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.empty());

			Optional<FeedbackResponseDto> expectedResponse = feedbackService.getFeedback(scheduleId);

			assertTrue(expectedResponse.isEmpty());
		}
	}

	@Nested
	@WithMockCustomUser
	@DisplayName("피드백 확인")
	class UpdateFeedbackTest {
		@Test
		@DisplayName("피드백 내역이 존재하면 멘티 피드백을 추가한다.")
		void when_feedbackExists_expect_updateFeedbackByMentee() {
			Feedback feedback = Feedback.of(1L, null, null);
			CoachSchedule coachSchedule = CoachSchedule.of(1L, 3L, LocalDateTime.now(), true);

			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.of(feedback));
			willDoNothing()
				.given(feedbackMapper).updateMenteeFeedbackByMenteeScheduleId(anyLong(), anyString());
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean())).willReturn(coachSchedule);

			feedbackService.updateFeedbackByMentee(4L, 1L, "멘티 피드백");

			verify(feedbackMapper)
				.updateMenteeFeedbackByMenteeScheduleId(1L, "멘티 피드백");
		}

		@Test
		@DisplayName("피드백 내역이 존재하지 않으면 NotFoundFeedbackException을 반환한다.")
		void when_feedbackDoesNotExistsForMentee_expect_throwsNotFoundFeedbackException() {
			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.empty());

			assertThatThrownBy(() ->
					feedbackService.updateFeedbackByMentee(4L, 1L, "멘티 피드백"))
				.isInstanceOf(NotFoundFeedbackException.class);
		}

		@Test
		@DisplayName("피드백 내역이 존재하면 apiType이 coaches일 때 코치 피드백을 추가한다.")
		void when_feedbackExistsAndApiTypeIsCoaches_expect_updateFeedbackByApiType() {
			Feedback feedback = Feedback.of(1L, null, null);
			MenteeSchedule menteeSchedule = MenteeSchedule.of(1L, 4L);
			CoachSchedule coachSchedule = CoachSchedule.of(1L, 3L, LocalDateTime.now(), true);

			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.of(feedback));
			willDoNothing()
				.given(feedbackMapper).updateCoachFeedbackByMenteeScheduleId(anyLong(), anyString());
			given(menteeScheduleService.getMenteeScheduleByScheduleId(anyLong())).willReturn(menteeSchedule);
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean())).willReturn(coachSchedule);

			feedbackService.updateFeedbackByCoach(3L, 1L, "코치 피드백");

			verify(feedbackMapper)
				.updateCoachFeedbackByMenteeScheduleId(1L, "코치 피드백");
		}

		@Test
		@DisplayName("피드백 내역이 존재하지 않으면 NotFoundFeedbackException을 반환한다.")
		void when_feedbackDoesNotExistsForCoach_expect_throwsNotFoundFeedbackException() {
			given(feedbackMapper.findByMenteeScheduleId(anyLong())).willReturn(Optional.empty());

			assertThatThrownBy(() ->
					feedbackService.updateFeedbackByCoach(3L, 1L, "코치 피드백"))
				.isInstanceOf(NotFoundFeedbackException.class);
		}
	}
}
