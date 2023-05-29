package cobook.buddywisdom.cancellation.controller;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.BDDMockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cobook.buddywisdom.cancellation.dto.request.CancelRequestDto;
import cobook.buddywisdom.cancellation.dto.request.ConfirmCancelRequestDto;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.service.CancelRequestService;
import cobook.buddywisdom.cancellation.vo.DirectionType;
import cobook.buddywisdom.util.WithMockCustomUser;

@AutoConfigureMybatis
@WebMvcTest(MenteeCancelRequestController.class)
public class MenteeCancelRequestControllerTest {

	@MockBean
	private CancelRequestService cancelRequestService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private final static String BASE_URL = "/api/v1/mentees/schedule";

	private static CancelRequestResponseDto cancelRequestResponseDto;

	@BeforeAll
	static void setUp() {
		cancelRequestResponseDto =
			new CancelRequestResponseDto(1, 1, 4, 3,
				"취소 사유", false, LocalDateTime.now(), LocalDateTime.now());
	}

	@Nested
	@DisplayName("코칭 취소 요청 내역 조회")
	@WithMockCustomUser(role = "MENTEE")
	class CancelRequestTest {
		@Test
		@DisplayName("인증된 멘티의 보낸 취소 내역을 조회하기 위해 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMenteeExistsAndDirectionTypeIsSent_expect_callMethodAndReturn200Ok() throws Exception {
			BDDMockito
				.given(cancelRequestService.getCancelRequest(anyLong(), eq(DirectionType.SENT)))
				.willReturn(List.of(cancelRequestResponseDto));

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_URL + "/cancel-request")
						.param("direction", String.valueOf(DirectionType.SENT)))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(cancelRequestService)
				.getCancelRequest(anyLong(), eq(DirectionType.SENT));
			response.andExpect(MockMvcResultMatchers.status().isOk());

		}

		@Test
		@DisplayName("인증된 멘티의 받은 취소 내역을 조회하기 위해 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMenteeExistsAndDirectionTypeIsReceived_expect_callMethodAndReturn200Ok() throws Exception {
			BDDMockito
				.given(cancelRequestService.getCancelRequest(anyLong(), eq(DirectionType.RECEIVED)))
				.willReturn(List.of(cancelRequestResponseDto));

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.get(BASE_URL + "/cancel-request")
							.param("direction", String.valueOf(DirectionType.RECEIVED)))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(cancelRequestService)
				.getCancelRequest(anyLong(), eq(DirectionType.RECEIVED));
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}

	@Nested
	@DisplayName("코칭 취소 요청")
	@WithMockCustomUser(role = "MENTEE")
	class CreateCancelRequestTest {
		@Test
		@DisplayName("유효한 취소 일정 정보와 사유가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdAndReasonOfCancelAreValid_expects_callMethodAndReturn200Ok() throws Exception {
			CancelRequestDto cancelRequestDto = new CancelRequestDto(1L, "취소 사유");

			BDDMockito
				.given(cancelRequestService.saveCancelRequestByMentee(anyLong(), anyLong(), anyString()))
				.willReturn(cancelRequestResponseDto);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.post(BASE_URL + "/cancel-request")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cancelRequestDto))
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(cancelRequestService)
				.saveCancelRequestByMentee(anyLong(), anyLong(), anyString());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("취소 일정 정보와 사유가 null 값이라면 400 Bad Request가 반환된다.")
		void when_scheduleIdAndReasonOfCancelAreNull_expects_return400BadRequest(String reason) throws Exception {
			CancelRequestDto cancelRequestDto = new CancelRequestDto(null, reason);

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.post(BASE_URL + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(cancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("코칭 취소 요청 확인")
	@WithMockCustomUser(role = "MENTEE")
	class UpdateCancelRequestTest {
		@Test
		@DisplayName("유효한 취소 요청 및 일정 정보가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_requestIdsArdValid_expects_callMethodAndReturns200Ok() throws Exception {
			ConfirmCancelRequestDto confirmCancelRequestDto =
				new ConfirmCancelRequestDto(1L, 1L);

			BDDMockito.willDoNothing()
				.given(cancelRequestService).confirmCancelRequest(anyLong(), anyLong(), anyLong());

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.patch(BASE_URL + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(confirmCancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(cancelRequestService)
				.confirmCancelRequest(anyLong(), anyLong(), anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("취소 요청 및 일정 정보가 null 값이라면 400 Bad Request가 반환된다.")
		void when_requestIdsArdNull_expects_return400BadRequest() throws Exception {
			ConfirmCancelRequestDto confirmCancelRequestDto =
				new ConfirmCancelRequestDto(null, null);

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.patch(BASE_URL + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(confirmCancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}
}
