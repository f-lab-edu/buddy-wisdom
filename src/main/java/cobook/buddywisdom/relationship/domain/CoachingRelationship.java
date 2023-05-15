package cobook.buddywisdom.relationship.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoachingRelationship {
	private Long id;
	private Long coachId;
	private Long menteeId;
	private boolean activeYn;
	private LocalDateTime startedAt;
	private LocalDateTime expiredAt;

	public static CoachingRelationship of(Long coachId, Long menteeId, boolean activeYn, LocalDateTime startedAt, LocalDateTime expiredAt) {
		CoachingRelationship coachingRelationship = new CoachingRelationship();
		coachingRelationship.coachId = coachId;
		coachingRelationship.menteeId = menteeId;
		coachingRelationship.activeYn = activeYn;
		coachingRelationship.startedAt = startedAt;
		coachingRelationship.expiredAt = expiredAt;
		return coachingRelationship;
	}
}
