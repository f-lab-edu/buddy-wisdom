package cobook.buddywisdom.mentee.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

import cobook.buddywisdom.mentee.dto.request.MenteeMonthlyScheduleRequestDto;
import cobook.buddywisdom.mentee.dto.request.UpdateMenteeScheduleRequestDto;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import cobook.buddywisdom.util.WithMockCustomUser;

@AutoConfigureMybatis
@WebMvcTest(MenteeController.class)
public class MenteeControllerTest {

	@MockBean
	private MenteeScheduleService menteeScheduleService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("월별 스케줄 조회")
	@WithMockCustomUser(role = "MENTEE")
	class MonthlyScheduleTest {
		@Test
		@DisplayName("일정 정보가 모두 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_dateIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
			LocalDateTime startDateTime = LocalDateTime.parse(firstDayOfMonth + "T00:00:00");
			LocalDateTime endDateTime = LocalDateTime.parse(LocalDate.now().withDayOfMonth(firstDayOfMonth.lengthOfMonth()) + "T23:59:59");

			MenteeMonthlyScheduleRequestDto request = new MenteeMonthlyScheduleRequestDto(startDateTime, endDateTime);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/monthly")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(request)))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).getMenteeMonthlySchedule(BDDMockito.anyLong(), BDDMockito.any());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("일정 정보가 null 값이라면 400 Bad Request가 반환된다.")
		void when_emailFieldIsNullAndEmptyAndBlank_expect_joinToFail() throws Exception {
			MenteeMonthlyScheduleRequestDto request = new MenteeMonthlyScheduleRequestDto(null, null);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/monthly")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(request)))
				.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("스케줄 피드백 조회")
	class ScheduleFeedbackTest {
		@Test
		@WithMockCustomUser(role = "MENTEE")
		@DisplayName("유효한 스케줄 id가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			Long scheduleId = 1L;

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + scheduleId))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).getMenteeScheduleFeedback(BDDMockito.anyLong(), BDDMockito.anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}
	@Nested
	@DisplayName("코칭 신청")
	@WithMockCustomUser(role = "MENTEE")
	class CreateScheduleTest {
		@Test
		@DisplayName("인증된 멘티 정보가 존재하면 메서드를 호출하고 200 OK를 반환한다.")
		void when_authenticatedMenteeExists_expect_callMethodAndReturn200Ok() throws Exception {
			Long authenticatedId = 1L;

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.get("/api/v1/mentees/schedule"))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).getMyCoachSchedule(authenticatedId);
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("유효한 스케줄 id가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			Long scheduleId = 1L;

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/v1/mentees/schedule/" + scheduleId)
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).saveMenteeSchedule(BDDMockito.anyLong(), BDDMockito.anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}

	@Nested
	@DisplayName("코칭 일정 변경")
	@WithMockCustomUser(role = "MENTEE")
	class UpdateScheduleTest {

		@Test
		@DisplayName("스케줄 정보가 모두 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleInformationIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			UpdateMenteeScheduleRequestDto request =
				new UpdateMenteeScheduleRequestDto(1L, 2L);

			BDDMockito.willDoNothing().given(menteeScheduleService).updateMenteeSchedule(BDDMockito.any());

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.patch("/api/v1/mentees/schedule")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).updateMenteeSchedule(BDDMockito.any(UpdateMenteeScheduleRequestDto.class));
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("스케줄 정보가 null 값이라면 400 Bad Request가 반환된다.")
		void when_scheduleInformationIsNull_expect_return400BadRequest() throws Exception {
			UpdateMenteeScheduleRequestDto request = new UpdateMenteeScheduleRequestDto(null, null);

			ResultActions response =
				mockMvc.perform(
						MockMvcRequestBuilders.patch("/api/v1/mentees/schedule")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request))
							.with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}
}
