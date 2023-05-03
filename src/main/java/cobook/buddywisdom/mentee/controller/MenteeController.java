package cobook.buddywisdom.mentee.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cobook.buddywisdom.mentee.annotation.YearMonth;
import cobook.buddywisdom.mentee.dto.MenteeMonthlyScheduleResponse;
import cobook.buddywisdom.mentee.dto.MenteeScheduleFeedbackResponse;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;

@Validated
@RestController
@RequestMapping("/api/v1/mentees")
public class MenteeController {

	private final MenteeScheduleService menteeScheduleService;

	public MenteeController(MenteeScheduleService menteeScheduleService) {
		this.menteeScheduleService = menteeScheduleService;
	}

	// TODO : menteeId는 인증된 객체의 정보 활용 -> 테스트 코드도 변경
	@GetMapping(value = "/schedule/{menteeId}/{date}")
	public ResponseEntity<Optional<List<MenteeMonthlyScheduleResponse>>> getMenteeMonthlySchedule(@PathVariable Long menteeId,
																								@PathVariable @YearMonth String date) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeMonthlySchedule(menteeId, date));
	}

	@GetMapping(value = "/schedule/feedback/{menteeId}/{scheduleId}")
	public ResponseEntity<MenteeScheduleFeedbackResponse> getMenteeScheduleFeedback(@PathVariable Long menteeId, @PathVariable Long scheduleId) {
		return ResponseEntity.ok(menteeScheduleService.getMenteeScheduleFeedback(menteeId, scheduleId));
	}

}
