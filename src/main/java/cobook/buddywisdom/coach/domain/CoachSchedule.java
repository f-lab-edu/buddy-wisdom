package cobook.buddywisdom.coach.domain;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CoachSchedule {
	private Long id;
	private Long coachId;
	private LocalDateTime possibleDateTime;
	private boolean matchYn;
}
