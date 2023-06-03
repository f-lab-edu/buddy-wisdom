package cobook.buddywisdom.mentee.service;


import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.util.MessageUtil;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.response.MenteeMonthlyScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleFeedbackResponseDto;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MyCoachScheduleResponseDto;
import cobook.buddywisdom.mentee.exception.DuplicatedMenteeScheduleException;
import cobook.buddywisdom.mentee.exception.NotAllowedUpdateException;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import cobook.buddywisdom.relationship.domain.CoachingRelationship;
import cobook.buddywisdom.relationship.exception.NotFoundRelationshipException;
import cobook.buddywisdom.relationship.service.CoachingRelationshipService;
import cobook.buddywisdom.util.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
public class MenteeScheduleServiceTest {

	@Mock
	MenteeScheduleMapper menteeScheduleMapper;

	@Mock
	CoachScheduleService coachScheduleService;

	@Mock
	CoachingRelationshipService coachingRelationshipService;

	@Mock
	FeedMessageProducer feedMessageProducer;

	@Mock
	MessageUtil messageUtil;

	@InjectMocks
	MenteeScheduleService menteeScheduleService;

	private static final long MENTEE_ID = 1;
	private static final long COACH_ID = 1;
	private static final long SCHEDULE_ID = 1;

	@Nested
	@DisplayName("월별 스케줄 조회")
	class MonthlyScheduleTest {
		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하면 월별 스케줄 정보를 반환한다.")
		void when_scheduleExistsWithInformation_expect_returnResponseList() {
			MenteeMonthlySchedule menteeMonthlySchedule =
				MenteeMonthlySchedule.of( SCHEDULE_ID, LocalDateTime.now());

			given(menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(anyLong(), any(), any()))
				.willReturn(List.of(menteeMonthlySchedule));

			List<MenteeMonthlyScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMenteeMonthlySchedule(MENTEE_ID, getMenteeMonthlyScheduleRequest());

			assertNotNull(expectedResponse);
			assertEquals(1, expectedResponse.size());
		}

		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 빈 배열을 반환한다.")
		void when_scheduleDoesNotExistsWithInformation_expect_returnEmptyArray() {
			given(menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(anyLong(), any(), any()))
				.willReturn(null);

			List<MenteeMonthlyScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMenteeMonthlySchedule(MENTEE_ID, getMenteeMonthlyScheduleRequest());

			assertTrue(expectedResponse.isEmpty());
		}

	}

	@Nested
	@DisplayName("스케줄 피드백 조회")
	class ScheduleFeedbackTest {
		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하면 스케줄 피드백 정보를 반환한다.")
		void when_scheduleExistsWithInformation_expect_returnResponse() {
			MenteeScheduleFeedback menteeScheduleFeedback =
				MenteeScheduleFeedback.of(SCHEDULE_ID, 1L, "코치 피드백", "멘티 피드백", LocalDateTime.now());

			given(menteeScheduleMapper.findWithFeedbackByMenteeIdAndCoachingScheduleId(anyLong(), anyLong()))
				.willReturn(Optional.of(menteeScheduleFeedback));

			MenteeScheduleFeedbackResponseDto expectedResponse =
				menteeScheduleService.getMenteeScheduleFeedback(MENTEE_ID, SCHEDULE_ID);

			assertNotNull(expectedResponse);
			assertEquals(menteeScheduleFeedback.getCoachingScheduleId(), expectedResponse.coachingScheduleId());
		}

		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_scheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
			given(menteeScheduleMapper.findWithFeedbackByMenteeIdAndCoachingScheduleId(anyLong(), anyLong()))
				.willThrow(NotFoundMenteeScheduleException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.getMenteeScheduleFeedback(MENTEE_ID, SCHEDULE_ID))
			.isInstanceOf(NotFoundMenteeScheduleException.class);
		}
	}

	@Nested
	@DisplayName("코칭 신청")
	class CreateScheduleTest {

		@Test
		@DisplayName("유효한 정보가 전달되면 매칭된 코치의 스케줄 정보를 반환한다.")
		void when_allInformationIsValid_expect_returnResponseList() {
			CoachingRelationship coachingRelationship =
				CoachingRelationship.of(COACH_ID, MENTEE_ID, true, LocalDateTime.now(), LocalDateTime.now().plusMonths(2));
			CoachSchedule coachSchedule = CoachSchedule.of(1L, COACH_ID, LocalDateTime.now(), false);

			given(coachingRelationshipService.getCoachingRelationshipByMenteeId(anyLong()))
					.willReturn(coachingRelationship);
			given(coachScheduleService.getAllCoachingSchedule(anyLong())).willReturn(List.of(coachSchedule));

			List<MyCoachScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMyCoachSchedule(MENTEE_ID);

			assertNotNull(expectedResponse);
			assertEquals(1, expectedResponse.size());
		}

		@Test
		@DisplayName("매칭된 코치 정보가 존재하지 않으면 NotFoundRelationshipException이 발생한다..")
		void when_matchingInformationNotExists_expect_throwsNotFoundRelationshipException() {
			given(coachingRelationshipService.getCoachingRelationshipByMenteeId(anyLong()))
				.willThrow(NotFoundRelationshipException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.getMyCoachSchedule(MENTEE_ID))
				.isInstanceOf(NotFoundRelationshipException.class);
		}

		@Test
		@DisplayName("유효한 정보가 전달되면 신청이 완료되고 스케줄 정보를 반환한다.")
		void when_informationIsValid_expect_createScheduleAndReturnMenteeSchedule() {
			MenteeSchedule menteeSchedule = MenteeSchedule.of(SCHEDULE_ID, MENTEE_ID);
			CoachSchedule coachSchedule = CoachSchedule.of(1L, COACH_ID, LocalDateTime.now(), false);

			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
					.willReturn(coachSchedule);
			willDoNothing().given(menteeScheduleMapper).save(any(MenteeSchedule.class));
			willDoNothing().given(coachScheduleService).updateMatchYn(anyLong(), anyBoolean());

			MenteeScheduleResponseDto expectedResponse =
				menteeScheduleService.saveMenteeSchedule(MENTEE_ID, SCHEDULE_ID);

			assertNotNull(expectedResponse);
			assertEquals(menteeSchedule.getMenteeId(), expectedResponse.menteeId());
			assertEquals(menteeSchedule.getCoachingScheduleId(), expectedResponse.coachingScheduleId());
		}

		@Test
		@DisplayName("해당하는 코칭 스케줄이 존재하지 않으면 NotFoundCoachScheduleException이 발생한다.")
		void when_coachScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			Long notExistScheduleId = 1L;

			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.saveMenteeSchedule(MENTEE_ID, notExistScheduleId))
				.isInstanceOf(NotFoundCoachScheduleException.class);
		}

		@Test
		@DisplayName("멘티 스케줄이 이미 존재하면 DuplicatedMenteeScheduleException이 발생한다.")
		void when_menteeScheduleExists_expect_throwsDuplicatedMenteeScheduleException() {
			given(menteeScheduleMapper.findByCoachingScheduleId(anyLong()))
				.willThrow(DuplicatedMenteeScheduleException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.checkMenteeScheduleNotExist(SCHEDULE_ID))
				.isInstanceOf(DuplicatedMenteeScheduleException.class);
		}
	}

	@Nested
	@DisplayName("코칭 일정 변경")
	@WithMockCustomUser(role = "MENTEE")
	class UpdateScheduleEventDetailsTest {

		@Test
		@DisplayName("스케줄 정보가 모두 전달되면 일정을 변경하고 각 스케줄의 상태 값을 변경한다.")
		void when_scheduleInformationIsValid_expect_callMethodAndReturn200Ok() {
			long currentCoachingId = 1L;
			long newCoachingId = 1L;

			CoachSchedule currentCoachSchedule = CoachSchedule.of(currentCoachingId, COACH_ID, LocalDateTime.now().plusDays(1), true);
			CoachSchedule newCoachSchedule = CoachSchedule.of(newCoachingId, COACH_ID, LocalDateTime.now().plusDays(1), false);

			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
					.willReturn(currentCoachSchedule);
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
					.willReturn(newCoachSchedule);
			willDoNothing().given(menteeScheduleMapper).updateCoachingScheduleId(anyLong(), anyLong());
			willDoNothing().given(coachScheduleService).updateMatchYn(anyLong(), anyBoolean());

			menteeScheduleService.updateMenteeSchedule(MENTEE_ID, currentCoachingId, newCoachingId);

			verify(coachScheduleService).getCoachSchedule(currentCoachingId, true);
			verify(coachScheduleService).getCoachSchedule(newCoachingId, false);
			verify(menteeScheduleMapper).updateCoachingScheduleId(currentCoachingId, newCoachingId);
			verify(coachScheduleService).updateMatchYn(currentCoachingId, false);
			verify(coachScheduleService).updateMatchYn(newCoachingId, true);
		}

		@Test
		@DisplayName("일정 변경 시간이 지났다면 NotAllowedUpdateException이 발생한다.")
		void when_timeHasPassed_expect_throwsNotAllowedUpdateException() {
			CoachSchedule currentCoachSchedule = CoachSchedule.of(1L, COACH_ID, LocalDateTime.now().minusDays(1), true);

			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willReturn(currentCoachSchedule);

			assertThatThrownBy(() ->
					menteeScheduleService.getScheduleIfUpdatePossible(SCHEDULE_ID))
				.isInstanceOf(NotAllowedUpdateException.class);
		}

		@Test
		@DisplayName("일정 변경 시간이 지나지 않았다면 변경 가능함이 확인된다.")
		void when_timeHasNotPassed_expect_validateUpdatePossibility() {
			CoachSchedule currentCoachSchedule = CoachSchedule.of(1L, COACH_ID, LocalDateTime.now().plusDays(1), true);

			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willReturn(currentCoachSchedule);

			menteeScheduleService.getScheduleIfUpdatePossible(SCHEDULE_ID);

			verify(coachScheduleService).getCoachSchedule(1L, true);
		}

		@Test
		@DisplayName("기존 스케줄 정보가 존재하지 않는다면 NotFoundCoachScheduleException이 발생한다.")
		void when_currentScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.getScheduleIfUpdatePossible(SCHEDULE_ID))
				.isInstanceOf(NotFoundCoachScheduleException.class);
		}

		@Test
		@DisplayName("변경하려는 스케줄 정보가 존재하지 않는다면 NotFoundCoachScheduleException이 발생한다.")
		void when_newScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			assertThatThrownBy(() ->
					menteeScheduleService.updateMenteeSchedule(MENTEE_ID, SCHEDULE_ID, SCHEDULE_ID))
				.isInstanceOf(NotFoundCoachScheduleException.class);
		}
	}

	@Nested
	@DisplayName("코칭 일정 취소")
	class DeleteScheduleTest {
		@Test
		@DisplayName("해당 스케줄이 존재하면 일정을 삭제한다.")
		void when_scheduleExists_expect_deleteSchedule() {
			MenteeSchedule menteeSchedule = MenteeSchedule.of(SCHEDULE_ID, MENTEE_ID);

			given(menteeScheduleMapper.findByCoachingScheduleId(anyLong()))
				.willReturn(Optional.of(menteeSchedule));

			menteeScheduleService.deleteMenteeSchedule(SCHEDULE_ID);

			verify(menteeScheduleMapper).findByCoachingScheduleId(SCHEDULE_ID);
		}

		@Test
		@DisplayName("해당 스케줄이 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_scheduleNotExists_expect_throwsNotFoundMenteeScheduleException() {
			given(menteeScheduleMapper.findByCoachingScheduleId(anyLong()))
				.willReturn(Optional.empty());

			assertThatThrownBy(() ->
					menteeScheduleService.deleteMenteeSchedule(SCHEDULE_ID))
				.isInstanceOf(NotFoundMenteeScheduleException.class);
		}
	}

	public static MenteeMonthlyScheduleRequestDto getMenteeMonthlyScheduleRequest() {
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfMonth = now.withDayOfMonth(1);
		LocalDateTime startDateTime = LocalDateTime.parse(firstDayOfMonth + "T00:00:00");
		LocalDateTime endDateTime = LocalDateTime.parse(
			now.withDayOfMonth(firstDayOfMonth.lengthOfMonth()) + "T23:59:59");

		return new MenteeMonthlyScheduleRequestDto(startDateTime, endDateTime);
	}
}
