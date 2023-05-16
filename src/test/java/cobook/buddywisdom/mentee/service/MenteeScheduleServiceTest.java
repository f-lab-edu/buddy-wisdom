package cobook.buddywisdom.mentee.service;


import java.time.LocalDate;
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

import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.mentee.domain.MenteeMonthlySchedule;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.domain.MenteeScheduleFeedback;
import cobook.buddywisdom.mentee.dto.response.MenteeMonthlyScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleFeedbackResponseDto;
import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.response.MenteeScheduleResponseDto;
import cobook.buddywisdom.mentee.dto.response.MyCoachScheduleResponseDto;
import cobook.buddywisdom.mentee.exception.DuplicatedMenteeScheduleException;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.mapper.MenteeScheduleMapper;
import cobook.buddywisdom.relationship.domain.CoachingRelationship;
import cobook.buddywisdom.relationship.exception.NotFoundRelationshipException;
import cobook.buddywisdom.relationship.service.CoachingRelationshipService;

@ExtendWith(MockitoExtension.class)
public class MenteeScheduleServiceTest {

	@Mock
	MenteeScheduleMapper menteeScheduleMapper;

	@Mock
	CoachScheduleService coachScheduleService;

	@Mock
	CoachingRelationshipService coachingRelationshipService;

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
			MenteeMonthlySchedule menteeMonthlySchedule = MenteeMonthlySchedule.of( SCHEDULE_ID, LocalDateTime.now());

			BDDMockito.given(menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any()))
				.willReturn(List.of(menteeMonthlySchedule));

			List<MenteeMonthlyScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMenteeMonthlySchedule(MENTEE_ID, getMenteeMonthlyScheduleRequest());

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(1, expectedResponse.size());
		}

		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 빈 배열을 반환한다.")
		void when_scheduleDoesNotExistsWithInformation_expect_returnEmptyArray() {
			BDDMockito
				.given(menteeScheduleMapper.findAllByMenteeIdAndPossibleDateTime(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any()))
				.willReturn(null);

			List<MenteeMonthlyScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMenteeMonthlySchedule(MENTEE_ID, getMenteeMonthlyScheduleRequest());

			Assertions.assertTrue(expectedResponse.isEmpty());
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

			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willReturn(Optional.of(menteeScheduleFeedback));

			MenteeScheduleFeedbackResponseDto expectedResponse =
				menteeScheduleService.getMenteeScheduleFeedback(MENTEE_ID, SCHEDULE_ID);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(menteeScheduleFeedback.getCoachingScheduleId(), expectedResponse.coachingScheduleId());
		}

		@Test
		@DisplayName("해당하는 스케줄 정보가 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_scheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
			BDDMockito.given(menteeScheduleMapper.findByMenteeIdAndCoachingScheduleId(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willThrow(NotFoundMenteeScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
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

			BDDMockito.given(coachingRelationshipService.getCoachingRelationshipByMenteeId(BDDMockito.anyLong()))
					.willReturn(coachingRelationship);
			BDDMockito.given(coachScheduleService.getAllCoachingSchedule(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any()))
					.willReturn(List.of(coachSchedule));

			List<MyCoachScheduleResponseDto> expectedResponse =
				menteeScheduleService.getMyCoachSchedule(MENTEE_ID);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(1, expectedResponse.size());
		}

		@Test
		@DisplayName("매칭된 코치 정보가 존재하지 않으면 NotFoundRelationshipException이 발생한다..")
		void when_matchingInformationNotExists_expect_throwsNotFoundRelationshipException() {
			BDDMockito.given(coachingRelationshipService.getCoachingRelationshipByMenteeId(BDDMockito.anyLong()))
				.willThrow(NotFoundRelationshipException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					menteeScheduleService.getMyCoachSchedule(MENTEE_ID))
				.isInstanceOf(NotFoundRelationshipException.class);
		}

		@Test
		@DisplayName("유효한 정보가 전달되면 신청이 완료되고 스케줄 정보를 반환한다.")
		void when_informationIsValid_expect_createScheduleAndReturnMenteeSchedule() {
			MenteeSchedule menteeSchedule = MenteeSchedule.of(SCHEDULE_ID, MENTEE_ID);
			CoachSchedule coachSchedule = CoachSchedule.of(1L, COACH_ID, LocalDateTime.now(), false);

			BDDMockito.given(coachScheduleService.getCoachSchedule(BDDMockito.anyLong(), BDDMockito.anyBoolean()))
					.willReturn(coachSchedule);
			BDDMockito.willDoNothing().given(menteeScheduleMapper).save(BDDMockito.any(MenteeSchedule.class));
			BDDMockito.willDoNothing().given(coachScheduleService).updateMatchYn(BDDMockito.anyLong(), BDDMockito.anyBoolean());

			MenteeScheduleResponseDto expectedResponse =
				menteeScheduleService.saveMenteeSchedule(MENTEE_ID, SCHEDULE_ID);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(menteeSchedule.getMenteeId(), expectedResponse.menteeId());
			Assertions.assertEquals(menteeSchedule.getCoachingScheduleId(), expectedResponse.coachingScheduleId());
		}

		@Test
		@DisplayName("해당하는 코칭 스케줄이 존재하지 않으면 NotFoundCoachScheduleException이 발생한다.")
		void when_coachScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			Long notExistScheduleId = 1L;

			BDDMockito.given(coachScheduleService.getCoachSchedule(BDDMockito.anyLong(), BDDMockito.anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					menteeScheduleService.saveMenteeSchedule(MENTEE_ID, notExistScheduleId))
				.isInstanceOf(NotFoundCoachScheduleException.class);
		}

		@Test
		@DisplayName("멘티 스케줄이 이미 존재하면 DuplicatedMenteeScheduleException이 발생한다.")
		void when_menteeScheduleExists_expect_throwsDuplicatedMenteeScheduleException() {
			Long scheduleId = 1L;

			BDDMockito.given(menteeScheduleMapper.findByCoachingScheduleId(BDDMockito.anyLong()))
				.willThrow(DuplicatedMenteeScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					menteeScheduleService.checkMenteeScheduleNotExist(scheduleId))
				.isInstanceOf(DuplicatedMenteeScheduleException.class);
		}
	}

	public static MenteeMonthlyScheduleRequestDto getMenteeMonthlyScheduleRequest() {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDateTime startDateTime = LocalDateTime.parse(firstDayOfMonth + "T00:00:00");
		LocalDateTime endDateTime = LocalDateTime.parse(LocalDate.now().withDayOfMonth(firstDayOfMonth.lengthOfMonth()) + "T23:59:59");

		return new MenteeMonthlyScheduleRequestDto(startDateTime, endDateTime);
	}
}