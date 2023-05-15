package cobook.buddywisdom.coach.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoachSchedule {
	private Long id;
	private Long coachId;
	private LocalDateTime possibleDateTime;
	private boolean matchYn;

	public static CoachSchedule of(Long coachId, LocalDateTime possibleDateTime, boolean matchYn) {
		CoachSchedule coachSchedule = new CoachSchedule();
		coachSchedule.coachId = coachId;
		coachSchedule.possibleDateTime = possibleDateTime;
		coachSchedule.matchYn = matchYn;
		return coachSchedule;

	}
}
