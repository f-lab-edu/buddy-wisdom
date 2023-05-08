//package cobook.buddywisdom.mentee.service;
//
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import org.assertj.core.api.AssertionsForClassTypes;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.BDDMockito;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
//import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
//import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
//import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
//import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequest;
//import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
//import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;
//
//@ExtendWith(MockitoExtension.class)
//public class MenteeServiceTest {
//
//	@Mock
//	MenteeScheduleMapper menteeScheduleMapper;
//
//	@InjectMocks
//	MenteeScheduleService menteeScheduleService;
//
//	@Nested
//	@DisplayName("월별 스케줄 조회")
//	class MonthlyScheduleTest {
//		@Test
//		@DisplayName("해당하는 스케줄 정보가 존재하면 월별 스케줄 정보를 반환한다.")
//		void when_scheduleExistsWithInformation_expect_returnResponseList() {
//			Long menteeId = 1L;
//
//			MenteeMonthlySchedule menteeMonthlySchedule = MenteeMonthlySchedule.of( 1L, false, LocalDateTime.now());
//
//			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any()))
//				.willReturn(menteeMonthlySchedule);
//
//			List<MenteeMonthlyScheduleResponse> expectedResponse =
//				menteeScheduleService.getMenteeMonthlySchedule(menteeId, getMenteeMonthlyScheduleRequest());
//
//			Assertions.assertNotNull(expectedResponse);
//			Assertions.assertEquals(1, expectedResponse.size());
//		}
//
//		@Test
//		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 빈 배열을 반환한다.")
//		void when_scheduleDoesNotExistsWithInformation_expect_returnEmptyArray() {
//			Long menteeId = 1L;
//
//			BDDMockito
//				.given(menteeScheduleMapper.findByMenteeIdAndPossibleDateTime(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any()))
//				.willReturn(null);
//
//			List<MenteeMonthlyScheduleResponse> expectedResponse =
//				menteeScheduleService.getMenteeMonthlySchedule(menteeId, getMenteeMonthlyScheduleRequest());
//
//			Assertions.assertTrue(expectedResponse.isEmpty());
//		}
//
//	}
//
//	@Nested
//	@DisplayName("스케줄 피드백 조회")
//	class ScheduleFeedbackTest {
//		@Test
//		@DisplayName("해당하는 스케줄 정보가 존재하면 스케줄 피드백 정보를 반환한다.")
//		void when_scheduleExistsWithInformation_expect_returnResponse() {
//			Long menteeId = 1L;
//			Long scheduleId = 1L;
//
//			MenteeScheduleFeedback menteeScheduleFeedback =
//				MenteeScheduleFeedback.of(1L, "코치 피드백", "멘티 피드백", LocalDateTime.now());
//
//			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
//				.willReturn(Optional.of(menteeScheduleFeedback));
//
//			MenteeScheduleFeedbackResponse expectedResponse =
//				menteeScheduleService.getMenteeScheduleFeedback(menteeId, scheduleId);
//
//			Assertions.assertNotNull(expectedResponse);
//			Assertions.assertEquals(menteeScheduleFeedback.getId(), expectedResponse.id());
//		}
//
//		@Test
//		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
//		void when_scheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
//			Long menteeId = 1L;
//			Long scheduleId = 1L;
//
//			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
//				.willThrow(NotFoundMenteeScheduleException.class);
//
//			AssertionsForClassTypes.assertThatThrownBy(() ->
//					menteeScheduleService.getMenteeScheduleFeedback(menteeId, scheduleId))
//			.isInstanceOf(NotFoundMenteeScheduleException.class);
//		}
//	}
//
//	public static MenteeMonthlyScheduleRequest getMenteeMonthlyScheduleRequest() {
//		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
//		LocalDateTime startDateTime = LocalDateTime.parse(firstDayOfMonth + "T00:00:00");
//		LocalDateTime endDateTime = LocalDateTime.parse(LocalDate.now().withDayOfMonth(firstDayOfMonth.lengthOfMonth()) + "T23:59:59");
//
//		return new MenteeMonthlyScheduleRequest(startDateTime, endDateTime);
//	}
//}
