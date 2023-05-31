package cobook.buddywisdom.cancellation.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import cobook.buddywisdom.feed.dto.ScheduleEventDto;
import cobook.buddywisdom.global.util.MessageFormatter;
import cobook.buddywisdom.global.util.ScheduleEventManager;
import cobook.buddywisdom.global.vo.ScheduleEventDetails;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelRequestEventService {

	private final FeedMessageProducer feedMessageProducer;
	private final ScheduleEventManager scheduleEventManager;

	public void produceCancelRequestEvent(long senderId, long receiverId, LocalDateTime localDateTime, String template) {
		ScheduleEventDetails scheduleEventDetails = ScheduleEventDetails.of(senderId, receiverId, template);

		MessageFormatter cancelRequestFormatter = (message, args) -> MessageFormat.format(message, args[0]);

		ScheduleEventDto scheduleEventDto =
			scheduleEventManager.createByScheduleDetails(scheduleEventDetails, cancelRequestFormatter, localDateTime);

		feedMessageProducer.produceScheduleEvent(scheduleEventDto);
	}
}
