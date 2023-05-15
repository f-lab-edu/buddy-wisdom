package cobook.buddywisdom.relationship.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.relationship.domain.CoachingRelationship;

@Mapper
public interface CoachingRelationshipMapper {
	Optional<CoachingRelationship> findByMenteeId(Long menteeId);
}
