package cobook.buddywisdom.feed.dto;

import lombok.Builder;

@Builder
public record ScheduleEventDto (
	long senderId,
	long receiverId,
	String feedMessage
) {
}
