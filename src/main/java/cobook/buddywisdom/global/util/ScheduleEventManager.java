package cobook.buddywisdom.global.util;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import cobook.buddywisdom.global.vo.ScheduleEventDetails;
import cobook.buddywisdom.feed.dto.ScheduleEventDto;
import cobook.buddywisdom.global.vo.UpdateScheduleEventDetails;

@Component
public class ScheduleEventManager {

	private final MessageUtil messageUtil;

	public ScheduleEventManager(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public ScheduleEventDto createByScheduleDetails(ScheduleEventDetails schedule, String template) {
		String convertedDateTime = messageUtil.convertToString(schedule.getScheduleDateTime());

		String message = MessageFormat.format(template, convertedDateTime);

		return new ScheduleEventDto(
			schedule.getSenderId(),
			schedule.getReceiverId(),
			message
		);
	}

	public ScheduleEventDto createByUpdateScheduleDetails(UpdateScheduleEventDetails updateScheduleEventDetails, String template) {
		ScheduleEventDetails scheduleEventDetails = updateScheduleEventDetails.getScheduleEventDetails();
		String current = messageUtil.convertToString(scheduleEventDetails.getScheduleDateTime());
		String newSchedule = messageUtil.convertToString(updateScheduleEventDetails.getNewCoachingDateTime());

		String message = MessageFormat.format(template, current, newSchedule);

		return new ScheduleEventDto(
			scheduleEventDetails.getSenderId(),
			scheduleEventDetails.getReceiverId(),
			message
		);
	}
}
