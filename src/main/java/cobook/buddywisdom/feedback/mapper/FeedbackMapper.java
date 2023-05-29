package cobook.buddywisdom.feedback.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.feedback.domain.Feedback;

@Mapper
public interface FeedbackMapper {
	Optional<Feedback> findByMenteeScheduleId(long menteeScheduleId);
	void updateCoachFeedbackByMenteeScheduleId(long menteeScheduleId, String feedback);
	void updateMenteeFeedbackByMenteeScheduleId(long menteeScheduleId, String feedback);
}
