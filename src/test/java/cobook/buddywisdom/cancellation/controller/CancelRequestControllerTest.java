package cobook.buddywisdom.cancellation.controller;

import static cobook.buddywisdom.cancellation.vo.DirectionType.*;
import static cobook.buddywisdom.global.vo.MemberApiType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cobook.buddywisdom.cancellation.dto.request.CancelRequestDto;
import cobook.buddywisdom.cancellation.dto.request.ConfirmCancelRequestDto;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.service.CancelRequestService;
import cobook.buddywisdom.util.WithMockCustomUser;

@AutoConfigureMybatis
@WebMvcTest(CancelRequestController.class)
public class CancelRequestControllerTest {

	@MockBean
	private CancelRequestService cancelRequestService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private final static String BASE_URL = "/api/v1/";

	private static CancelRequestResponseDto cancelRequestResponseDto;

	@BeforeAll
	static void setUp() {
		cancelRequestResponseDto =
			new CancelRequestResponseDto(1, 1, 4, 3,
				"취소 사유", false, LocalDateTime.now(), LocalDateTime.now());
	}

	@Nested
	@DisplayName("코칭 취소 요청 내역 조회")
	@WithMockCustomUser()
	class CancelRequestTest {
		@ParameterizedTest
		@ValueSource(strings = {"mentees", "coaches"})
		@DisplayName("인증된 회원의 보낸 취소 내역을 조회하기 위해 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMenteeExistsAndDirectionTypeIsSent_expect_callMethodAndReturn200Ok(String type) throws Exception {
			given(cancelRequestService.getCancelRequest(anyLong(), eq(SENT)))
				.willReturn(List.of(cancelRequestResponseDto));

			ResultActions response =
				mockMvc.perform(get(BASE_URL + type + "/cancel-request")
						.param("direction", String.valueOf(SENT)))
				.andDo(print());

			verify(cancelRequestService).getCancelRequest(anyLong(), eq(SENT));
			response.andExpect(status().isOk());

		}

		@ParameterizedTest
		@ValueSource(strings = {"mentees", "coaches"})
		@DisplayName("인증된 회원의 받은 취소 내역을 조회하기 위해 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMenteeExistsAndDirectionTypeIsReceived_expect_callMethodAndReturn200Ok(String type) throws Exception {
			given(cancelRequestService.getCancelRequest(anyLong(), eq(RECEIVED)))
				.willReturn(List.of(cancelRequestResponseDto));

			ResultActions response =
				mockMvc.perform(get(BASE_URL + type + "/cancel-request")
							.param("direction", String.valueOf(RECEIVED)))
					.andDo(print());

			verify(cancelRequestService).getCancelRequest(anyLong(), eq(RECEIVED));
			response.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("멘티 코칭 취소 요청")
	@WithMockCustomUser(role = "MENTEE")
	class CreateCancelRequestTest {
		@Test
		@DisplayName("유효한 취소 일정 정보와 사유가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdAndReasonOfCancelAreValid_expects_callMethodAndReturn200Ok() throws Exception {
			CancelRequestDto cancelRequestDto = new CancelRequestDto(1L, "취소 사유");

			given(cancelRequestService.saveCancelRequestByMentee(anyLong(), anyLong(), anyString()))
				.willReturn(cancelRequestResponseDto);

			ResultActions response =
				mockMvc.perform(post(BASE_URL + MENTEES + "/cancel-request")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cancelRequestDto))
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(print());

			verify(cancelRequestService)
				.saveCancelRequestByMentee(anyLong(), anyLong(), anyString());
			response.andExpect(status().isOk());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName("취소 일정 정보와 사유가 null 값이라면 400 Bad Request가 반환된다.")
		void when_scheduleIdAndReasonOfCancelAreNull_expects_return400BadRequest(String reason) throws Exception {
			CancelRequestDto cancelRequestDto = new CancelRequestDto(null, reason);

			ResultActions response =
				mockMvc.perform(post(BASE_URL + MENTEES + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(cancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(print());

			response.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("멘티 코칭 취소 요청 확인")
	@WithMockCustomUser(role = "MENTEE")
	class UpdateCancelRequestTest {
		@Test
		@DisplayName("회원의 유효한 취소 요청 및 일정 정보가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_requestIdsArdValidForMentee_expects_callMethodAndReturns200Ok() throws Exception {
			ConfirmCancelRequestDto confirmCancelRequestDto =
				new ConfirmCancelRequestDto(1L, 1L);

			willDoNothing()
				.given(cancelRequestService).confirmCancelRequestByMentee(anyLong(), anyLong(), anyLong());
			willDoNothing()
				.given(cancelRequestService).confirmCancelRequest(anyLong(), anyLong());

			ResultActions response =
				mockMvc.perform(patch(BASE_URL + MENTEES + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(confirmCancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(print());

			verify(cancelRequestService)
				.confirmCancelRequestByMentee(anyLong(), anyLong(), anyLong());
			response.andExpect(status().isOk());
		}

		@Test
		@DisplayName("취소 요청 및 일정 정보가 null 값이라면 400 Bad Request가 반환된다.")
		void when_requestIdsArdNull_expects_return400BadRequest() throws Exception {
			ConfirmCancelRequestDto confirmCancelRequestDto =
				new ConfirmCancelRequestDto(null, null);

			ResultActions response =
				mockMvc.perform(patch(BASE_URL + MENTEES + "/cancel-request")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(confirmCancelRequestDto))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(print());

			response.andExpect(status().isBadRequest());
		}
	}
}
