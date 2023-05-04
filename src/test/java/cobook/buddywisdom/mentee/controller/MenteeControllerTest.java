package cobook.buddywisdom.mentee.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;

@AutoConfigureMybatis
@WebMvcTest(MenteeController.class)
public class MenteeControllerTest {

	@MockBean
	private MenteeScheduleService menteeScheduleService;

	@Autowired
	private MockMvc mockMvc;

	@Nested
	@DisplayName("월별 스케줄 조회")
	class MonthlyScheduleTest {
		@Test
		@DisplayName("해당 년도/월에 스케줄 정보가 존재하면 조회가 성공한다.")
		void when_scheduleExistsWithInformation_expect_selectToSuccess() throws Exception {
			Long menteeId = 1L;
			String date = "2023-04";

			MenteeMonthlyScheduleResponse expectedResponse =
				new MenteeMonthlyScheduleResponse(1L, 1L, false, LocalDateTime.now());

			BDDMockito
				.given(menteeScheduleService.getMenteeMonthlySchedule(BDDMockito.anyLong(), BDDMockito.anyString()))
				.willReturn(List.of(expectedResponse));


			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + menteeId + "/" + date))
				.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("해당 년도/월에 스케줄 정보가 존재하지 않으면 null을 반환한다.")
		void when_scheduleDoesNotExistsWithInformation_expect_selectToSuccessAndReturnNull() throws Exception {
			Long menteeId = 1L;
			String date = "2022-04";

			BDDMockito
				.given(menteeScheduleService.getMenteeMonthlySchedule(BDDMockito.anyLong(), BDDMockito.anyString()))
				.willReturn(null);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + menteeId + "/" + date))
				.andDo(MockMvcResultHandlers.print());

			response
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("null"));
		}

		@ParameterizedTest
		@ValueSource(strings = {"2023", "2023-4", "2023-04-24"})
		@DisplayName("해당 년도/월 매개변수의 유효성 검증을 통과하지 못하면 조회가 실패한다.")
		void when_dateIsInvalid_expect_selectToFail(String date) throws Exception {
			Long menteeId = 1L;

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + menteeId + "/" + date))
				.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("스케줄 피드백 조회")
	class ScheduleFeedbackTest {
		@Test
		@DisplayName("스케줄 정보가 존재하면 조회가 성공한다.")
		void when_scheduleExistsWithInformation_expect_selectToSuccess() throws Exception {
			Long menteeId = 1L;
			Long scheduleId = 1L;

			MenteeScheduleFeedbackResponse menteeScheduleFeedbackResponse =
				new MenteeScheduleFeedbackResponse(1L, LocalDateTime.now(), 1L, null, null);

			BDDMockito
				.given(menteeScheduleService.getMenteeScheduleFeedback(BDDMockito.anyLong(), BDDMockito.anyLong()))
				.willReturn(menteeScheduleFeedbackResponse);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/feedback/" + menteeId + "/" + scheduleId))
				.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}
}
