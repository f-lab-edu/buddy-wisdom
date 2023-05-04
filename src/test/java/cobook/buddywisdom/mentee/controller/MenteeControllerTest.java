package cobook.buddywisdom.mentee.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
		@DisplayName("유효한 년도/월 정보가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_dateIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			Long menteeId = 1L;
			String date = String.valueOf(LocalDateTime.now()).substring(0, 7);

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + menteeId + "/" + date))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).getMenteeMonthlySchedule(BDDMockito.anyLong(), BDDMockito.anyString());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}

		@Test
		@DisplayName("해당 년도/월 정보가 유효하지 않으면 400 Bad request가 반환된다.")
		void when_dateIsInvalid_expect_return400BadRequest() throws Exception {
			Long menteeId = 1L;
			String invalidDate = String.valueOf(LocalDateTime.now().getYear());

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/" + menteeId + "/" + invalidDate))
				.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("스케줄 피드백 조회")
	class ScheduleFeedbackTest {
		@Test
		@DisplayName("유효한 스케줄 id가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
		void when_scheduleIdIsValid_expect_callMethodAndReturn200Ok() throws Exception {
			Long menteeId = 1L;
			Long scheduleId = 1L;

			ResultActions response =
				mockMvc.perform(
					MockMvcRequestBuilders.get("/api/v1/mentees/schedule/feedback/" + menteeId + "/" + scheduleId))
				.andDo(MockMvcResultHandlers.print());

			BDDMockito.verify(menteeScheduleService).getMenteeScheduleFeedback(BDDMockito.anyLong(), BDDMockito.anyLong());
			response.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}
}
