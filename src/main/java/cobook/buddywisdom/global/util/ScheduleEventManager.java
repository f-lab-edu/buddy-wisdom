package cobook.buddywisdom.global.util;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import cobook.buddywisdom.global.vo.ScheduleEventDetails;
import cobook.buddywisdom.feed.dto.ScheduleEventDto;

@Component
public class ScheduleEventManager {

	private final MessageUtil messageUtil;

	public ScheduleEventManager(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public ScheduleEventDto createByScheduleDetails(ScheduleEventDetails eventDetails, MessageFormatter messageFormatter,
													LocalDateTime ...args) {
		String[] convertedDateTime = Arrays.stream(args)
			.map(messageUtil::convertToString)
			.toArray(String[]::new);

		return new ScheduleEventDto(
			eventDetails.getSenderId(),
			eventDetails.getReceiverId(),
			messageFormatter.format(eventDetails.getTemplate(), convertedDateTime)
		);
	}
}
