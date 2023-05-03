package cobook.buddywisdom.mentee.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;

@ExtendWith(MockitoExtension.class)
public class MenteeServiceTest {

	@Mock
	MenteeScheduleMapper menteeScheduleMapper;

	@InjectMocks
	MenteeScheduleService menteeScheduleService;

	@Test
	@DisplayName("유효한 값이 전달되면 조회된 월별 스케줄 정보를 반환한다.")
	void when_scheduleExistsWithInformation_expect_returnResponseList() {
		Long menteeId = 1L;
		String date = "2023-04";

		MenteeMonthlySchedule menteeMonthlySchedule = MenteeMonthlySchedule.of( 1L, false, LocalDateTime.now());

		BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(BDDMockito.anyLong(), BDDMockito.anyString()))
			.willReturn(menteeMonthlySchedule);

		Optional<List<MenteeMonthlyScheduleResponse>> expectedResponse =
			menteeScheduleService.getMenteeMonthlySchedule(menteeId, date);

		Assertions.assertNotNull(expectedResponse);
		Assertions.assertEquals(1, expectedResponse.get().size());
	}

	@Nested
	@DisplayName("스케줄 피드백 조회")
	class ScheduleFeedbackTest {
		@Test
		@DisplayName("등록된 스케줄 정보가 존재하면 스케줄 피드백 정보를 반환한다.")
		void when_scheduleExistsWithInformation_expect_returnResponse() {
			Long menteeId = 1L;
			Long scheduleId = 1L;

			MenteeScheduleFeedback menteeScheduleFeedback =
				MenteeScheduleFeedback.of(1L, "코치 피드백", "멘티 피드백", LocalDateTime.now());

			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willReturn(Optional.of(menteeScheduleFeedback));

			MenteeScheduleFeedbackResponse expectedResponse =
				menteeScheduleService.getMenteeScheduleFeedback(menteeId, scheduleId);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(menteeScheduleFeedback.getId(), expectedResponse.id());
		}

		@Test
		@DisplayName("등록된 스케줄 정보가 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_scheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
			Long menteeId = 1L;
			Long scheduleId = 1L;

			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willThrow(NotFoundMenteeScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					menteeScheduleService.getMenteeScheduleFeedback(menteeId, scheduleId))
			.isInstanceOf(NotFoundMenteeScheduleException.class);
		}
	}
}
