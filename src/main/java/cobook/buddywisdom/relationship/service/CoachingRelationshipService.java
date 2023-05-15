package cobook.buddywisdom.relationship.service;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.coach.exception.NotFoundCoachScheduleException;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.relationship.domain.CoachingRelationship;
import cobook.buddywisdom.relationship.exception.NotFoundRelationshipException;
import cobook.buddywisdom.relationship.mapper.CoachingRelationshipMapper;

@Service
public class CoachingRelationshipService {

	private final CoachingRelationshipMapper coachingRelationshipMapper;

	public CoachingRelationshipService(CoachingRelationshipMapper coachingRelationshipMapper) {
		this.coachingRelationshipMapper = coachingRelationshipMapper;
	}

	public CoachingRelationship getCoachingRelationshipByMenteeId(Long menteeId) {
		return coachingRelationshipMapper.findByMenteeId(menteeId)
			.orElseThrow(() -> new NotFoundRelationshipException(ErrorMessage.NOT_FOUND_COACHING_RELATIONSHIP));
	}
}
