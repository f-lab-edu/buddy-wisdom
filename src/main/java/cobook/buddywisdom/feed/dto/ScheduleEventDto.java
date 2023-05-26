package cobook.buddywisdom.feed.dto;

public record ScheduleEventDto (
	long senderId,
	long receiverId,
	String feedMessage
) {
}
