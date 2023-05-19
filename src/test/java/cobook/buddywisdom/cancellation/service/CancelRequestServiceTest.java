package cobook.buddywisdom.cancellation.service;

import static cobook.buddywisdom.cancellation.controller.DirectionType.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import cobook.buddywisdom.cancellation.domain.CancelRequest;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.exception.ConfirmedCancelRequestException;
import cobook.buddywisdom.cancellation.exception.NotFoundCancelRequestException;
import cobook.buddywisdom.cancellation.mapper.CancelRequestMapper;
import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;

@ExtendWith(MockitoExtension.class)
public class CancelRequestServiceTest {

	@Mock
	CancelRequestMapper cancelRequestMapper;

	@Mock
	MenteeScheduleService menteeScheduleService;

	@Mock
	CoachScheduleService coachScheduleService;

	@InjectMocks
	CancelRequestService cancelRequestService;

	private static CancelRequest cancelRequest;

	@BeforeAll
	static void setUp() {
		cancelRequest = CancelRequest.of(1L, 1L, 4L, 3L,
				"취소 사유", false, LocalDateTime.now(), LocalDateTime.now());
	}

	@Nested
	@DisplayName("코칭 취소 요청 내역 조회")
	class CancelRequestTest {
		@Test
		@DisplayName("보낸 취소 요청 내역이 존재하면 취소 요청 정보를 반환한다.")
		void when_sentRequestsExist_expect_returnResponseList() {
			BDDMockito.given(cancelRequestMapper.findBySenderId(BDDMockito.anyLong()))
				.willReturn(List.of(cancelRequest));

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, SENT);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(1, expectedResponse.size());

			CancelRequestResponseDto returnedDto = expectedResponse.get(0);
			Assertions.assertEquals(cancelRequest.getMenteeScheduleId(), returnedDto.menteeScheduleId());
			Assertions.assertEquals(cancelRequest.getSenderId(), returnedDto.senderId());
			Assertions.assertEquals(cancelRequest.getReceiverId(), returnedDto.receiverId());
			Assertions.assertEquals(cancelRequest.getReason(), returnedDto.reason());
		}

		@Test
		@DisplayName("보낸 취소 요청 내역이 존재하지 않으면 빈 배열을 반환한다.")
		void when_sentRequestsNotExist_expect_returnEmptyArray() {
			BDDMockito.given(cancelRequestMapper.findBySenderId(BDDMockito.anyLong()))
				.willReturn(Collections.emptyList());

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, SENT);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertTrue(expectedResponse.isEmpty());
		}

		@Test
		@DisplayName("받은 취소 요청 내역이 존재하면 취소 요청 정보를 반환한다.")
		void when_receivedRequestsExist_expect_returnResponseList() {
			BDDMockito.given(cancelRequestMapper.findByReceiverId(BDDMockito.anyLong()))
				.willReturn(List.of(cancelRequest));

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, RECEIVED);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(1, expectedResponse.size());

			CancelRequestResponseDto returnedDto = expectedResponse.get(0);
			Assertions.assertEquals(cancelRequest.getMenteeScheduleId(), returnedDto.menteeScheduleId());
			Assertions.assertEquals(cancelRequest.getSenderId(), returnedDto.senderId());
			Assertions.assertEquals(cancelRequest.getReceiverId(), returnedDto.receiverId());
			Assertions.assertEquals(cancelRequest.getReason(), returnedDto.reason());
		}

		@Test
		@DisplayName("받은 취소 요청 내역이 존재하지 않으면 빈 배열을 반환한다.")
		void when_receivedRequestsNotExist_expect_returnEmptyArray() {
			BDDMockito.given(cancelRequestMapper.findByReceiverId(BDDMockito.anyLong()))
				.willReturn(Collections.emptyList());

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, RECEIVED);

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertTrue(expectedResponse.isEmpty());
		}
	}

	@Nested
	@DisplayName("코칭 취소 요청")
	class CreateCancelRequestTest {
		@Test
		@DisplayName("유효한 정보가 전달되면 요청이 완료되고 취소 요청 정보를 반환한다.")
		void when_allInformationIsValid_expect_returnResponse() {
			MenteeSchedule menteeSchedule = MenteeSchedule.of(1L, 4L);
			CoachSchedule coachSchedule = CoachSchedule.of(1L, 3L, LocalDateTime.now(), false);

			BDDMockito.given(menteeScheduleService.getMenteeSchedule(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willReturn(menteeSchedule);
			BDDMockito.given(coachScheduleService.getCoachSchedule(BDDMockito.anyLong(), BDDMockito.anyBoolean()))
				.willReturn(coachSchedule);
			BDDMockito.doAnswer(invocation -> {
				CancelRequest savedCancelRequest = invocation.getArgument(0);
				BeanUtils.copyProperties(cancelRequest, savedCancelRequest);
				return null;
			}).when(cancelRequestMapper).save(BDDMockito.any(CancelRequest.class));


			CancelRequestResponseDto expectedResponse =
				cancelRequestService.saveCancelRequestByMentee(3L, 1L, "취소 사유");

			Assertions.assertNotNull(expectedResponse);
			Assertions.assertEquals(cancelRequest.getId(), expectedResponse.menteeScheduleId());
		}

		@Test
		@DisplayName("해당하는 멘티 스케줄이 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_menteeScheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
			BDDMockito.given(menteeScheduleService.getMenteeSchedule(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willThrow(NotFoundMenteeScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					cancelRequestService.saveCancelRequestByMentee(4L, 1L, "취소 사유"))
				.isInstanceOf(NotFoundMenteeScheduleException.class);
		}

		@Test
		@DisplayName("해당하는 코칭 스케줄이 존재하지 않으면 NotFoundCoachScheduleException이 발생한다.")
		void when_coachScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			BDDMockito.given(coachScheduleService.getCoachSchedule(BDDMockito.anyLong(), BDDMockito.anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			AssertionsForClassTypes.assertThatThrownBy(() ->
					cancelRequestService.saveCancelRequestByMentee(4L, 1L, "취소 사유"))
				.isInstanceOf(NotFoundCoachScheduleException.class);
		}
	}

	@Nested
	@DisplayName("코칭 취소 요청 확인")
	class UpdateCancelRequestTest {
		@Test
		@DisplayName("취소 요청 및 일정 정보가 전달되면 상태 값을 변경하고 해당 스케줄을 삭제한다.")
		void when_requestIdsArdValid_expect_updateConfirmYnAndDeleteSchedule() {
			BDDMockito.given(cancelRequestMapper.findById(BDDMockito.anyLong()))
				.willReturn(Optional.ofNullable(cancelRequest));
			BDDMockito.willDoNothing().
				given(cancelRequestMapper).updateConfirmYn(BDDMockito.anyLong(), BDDMockito.anyBoolean());
			BDDMockito.willDoNothing()
				.given(menteeScheduleService).deleteMenteeSchedule(BDDMockito.anyLong());

			cancelRequestService.confirmCancelRequest(1L, 1L);

			BDDMockito.verify(cancelRequestMapper).findById(1L);
			BDDMockito.verify(cancelRequestMapper).updateConfirmYn(1L, true);
			BDDMockito.verify(menteeScheduleService).deleteMenteeSchedule(1L);
		}

		@Test
		@DisplayName("취소 요청 내역이 존재하지 않으면 NotFoundCancelRequestException이 발생한다.")
		void when_cancelRequestNotExists_expect_throwsNotFoundCancelRequestException() {
			BDDMockito.given(cancelRequestMapper.findById(BDDMockito.anyLong()))
				.willReturn(Optional.empty());

			AssertionsForClassTypes.assertThatThrownBy(() ->
					cancelRequestService.confirmCancelRequest(1L, 1L))
				.isInstanceOf(NotFoundCancelRequestException.class);
		}

		@Test
		@DisplayName("이미 확인된 취소 요청이라면 ConfirmedCancelRequestException이 발생한다.")
		void when_confirmStatusIsTrue_expect_throwsConfirmedCancelRequestException() {
			CancelRequest confirmedRequest = CancelRequest.of(1L, 1L, 4L, 3L,
				"취소 사유", true, LocalDateTime.now(), LocalDateTime.now());

			BDDMockito.given(cancelRequestMapper.findById(BDDMockito.anyLong()))
				.willReturn(Optional.of(confirmedRequest));

			AssertionsForClassTypes.assertThatThrownBy(() ->
					cancelRequestService.confirmCancelRequest(1L, 1L))
				.isInstanceOf(ConfirmedCancelRequestException.class);
		}
	}
}
