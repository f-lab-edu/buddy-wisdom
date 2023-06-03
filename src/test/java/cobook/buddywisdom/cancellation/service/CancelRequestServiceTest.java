package cobook.buddywisdom.cancellation.service;

import static cobook.buddywisdom.cancellation.vo.DirectionType.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import cobook.buddywisdom.global.util.MessageUtil;
import cobook.buddywisdom.mentee.domain.MenteeSchedule;
import cobook.buddywisdom.mentee.exception.NotFoundMenteeScheduleException;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;

@ExtendWith(MockitoExtension.class)
public class CancelRequestServiceTest {

	@Mock
	CancelRequestMapper cancelRequestMapper;

	@Mock
	MenteeScheduleService menteeScheduleService;

	@Mock
	CoachScheduleService coachScheduleService;

	@Mock
	MessageUtil messageUtil;

	@Mock
	FeedMessageProducer feedMessageProducer;

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
			given(cancelRequestMapper.findBySenderId(anyLong()))
				.willReturn(List.of(cancelRequest));

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, SENT);

			assertNotNull(expectedResponse);
			assertEquals(1, expectedResponse.size());

			CancelRequestResponseDto returnedDto = expectedResponse.get(0);
			assertEquals(cancelRequest.getMenteeScheduleId(), returnedDto.menteeScheduleId());
			assertEquals(cancelRequest.getSenderId(), returnedDto.senderId());
			assertEquals(cancelRequest.getReceiverId(), returnedDto.receiverId());
			assertEquals(cancelRequest.getReason(), returnedDto.reason());
		}

		@Test
		@DisplayName("보낸 취소 요청 내역이 존재하지 않으면 빈 배열을 반환한다.")
		void when_sentRequestsNotExist_expect_returnEmptyArray() {
			given(cancelRequestMapper.findBySenderId(anyLong()))
				.willReturn(Collections.emptyList());

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, SENT);

			assertNotNull(expectedResponse);
			assertTrue(expectedResponse.isEmpty());
		}

		@Test
		@DisplayName("받은 취소 요청 내역이 존재하면 취소 요청 정보를 반환한다.")
		void when_receivedRequestsExist_expect_returnResponseList() {
			given(cancelRequestMapper.findByReceiverId(anyLong()))
				.willReturn(List.of(cancelRequest));

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, RECEIVED);

			assertNotNull(expectedResponse);
			assertEquals(1, expectedResponse.size());

			CancelRequestResponseDto returnedDto = expectedResponse.get(0);
			assertEquals(cancelRequest.getMenteeScheduleId(), returnedDto.menteeScheduleId());
			assertEquals(cancelRequest.getSenderId(), returnedDto.senderId());
			assertEquals(cancelRequest.getReceiverId(), returnedDto.receiverId());
			assertEquals(cancelRequest.getReason(), returnedDto.reason());
		}

		@Test
		@DisplayName("받은 취소 요청 내역이 존재하지 않으면 빈 배열을 반환한다.")
		void when_receivedRequestsNotExist_expect_returnEmptyArray() {
			given(cancelRequestMapper.findByReceiverId(anyLong()))
				.willReturn(Collections.emptyList());

			List<CancelRequestResponseDto> expectedResponse =
				cancelRequestService.getCancelRequest(4L, RECEIVED);

			assertNotNull(expectedResponse);
			assertTrue(expectedResponse.isEmpty());
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

			given(menteeScheduleService.getMenteeSchedule(anyLong(), anyLong()))
				.willReturn(menteeSchedule);
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willReturn(coachSchedule);
			doAnswer(invocation -> {
				CancelRequest savedCancelRequest = invocation.getArgument(0);
				BeanUtils.copyProperties(cancelRequest, savedCancelRequest);
				return null;
			}).when(cancelRequestMapper).save(any(CancelRequest.class));


			CancelRequestResponseDto expectedResponse =
				cancelRequestService.saveCancelRequestByMentee(3L, 1L, "취소 사유");

			assertNotNull(expectedResponse);
			assertEquals(cancelRequest.getId(), expectedResponse.menteeScheduleId());
		}

		@Test
		@DisplayName("해당하는 멘티 스케줄이 존재하지 않으면 NotFoundMenteeScheduleException이 발생한다.")
		void when_menteeScheduleDoesNotExists_expect_throwsNotFoundMenteeScheduleException() {
			given(menteeScheduleService.getMenteeSchedule(anyLong(), anyLong()))
				.willThrow(NotFoundMenteeScheduleException.class);

			assertThatThrownBy(() ->
					cancelRequestService.saveCancelRequestByMentee(4L, 1L, "취소 사유"))
				.isInstanceOf(NotFoundMenteeScheduleException.class);
		}

		@Test
		@DisplayName("해당하는 코칭 스케줄이 존재하지 않으면 NotFoundCoachScheduleException이 발생한다.")
		void when_coachScheduleNotExists_expect_throwsNotFoundCoachScheduleException() {
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willThrow(NotFoundCoachScheduleException.class);

			assertThatThrownBy(() ->
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
			CoachSchedule coachSchedule = CoachSchedule.of(1L, 3L, LocalDateTime.now(), false);

			given(cancelRequestMapper.findById(anyLong()))
				.willReturn(Optional.ofNullable(cancelRequest));
			willDoNothing().given(cancelRequestMapper).updateConfirmYn(anyLong(), anyBoolean());
			willDoNothing().given(menteeScheduleService).deleteMenteeSchedule(anyLong());
			given(coachScheduleService.getCoachSchedule(anyLong(), anyBoolean()))
				.willReturn(coachSchedule);

			cancelRequestService.confirmCancelRequest(1L, 1L, 1L);

			verify(cancelRequestMapper).findById(1L);
			verify(cancelRequestMapper).updateConfirmYn(1L, true);
			verify(menteeScheduleService).deleteMenteeSchedule(1L);
		}

		@Test
		@DisplayName("취소 요청 내역이 존재하지 않으면 NotFoundCancelRequestException이 발생한다.")
		void when_cancelRequestNotExists_expect_throwsNotFoundCancelRequestException() {
			given(cancelRequestMapper.findById(anyLong()))
				.willReturn(Optional.empty());

			assertThatThrownBy(() ->
					cancelRequestService.confirmCancelRequest(1L, 1L, 1L))
				.isInstanceOf(NotFoundCancelRequestException.class);
		}

		@Test
		@DisplayName("이미 확인된 취소 요청이라면 ConfirmedCancelRequestException이 발생한다.")
		void when_confirmStatusIsTrue_expect_throwsConfirmedCancelRequestException() {
			CancelRequest confirmedRequest = CancelRequest.of(1L, 1L, 4L, 3L,
				"취소 사유", true, LocalDateTime.now(), LocalDateTime.now());

			given(cancelRequestMapper.findById(anyLong()))
				.willReturn(Optional.of(confirmedRequest));

			assertThatThrownBy(() ->
					cancelRequestService.confirmCancelRequest(1L, 1L, 1L))
				.isInstanceOf(ConfirmedCancelRequestException.class);
		}
	}
}
