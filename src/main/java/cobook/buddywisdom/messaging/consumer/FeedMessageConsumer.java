package cobook.buddywisdom.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cobook.buddywisdom.feed.service.FeedService;
import cobook.buddywisdom.feed.dto.ScheduleEventDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedMessageConsumer {

	private final FeedService feedService;

	private static final String SCHEDULE_QUEUE = "schedule.events";

	@RabbitListener(queues = SCHEDULE_QUEUE)
	public void consumeFromScheduleEvents(ScheduleEventDto scheduleEventDto) {
		feedService.saveFeed(
			scheduleEventDto.senderId(),
			scheduleEventDto.receiverId(),
			scheduleEventDto.feedMessage()
		);
	}
}
