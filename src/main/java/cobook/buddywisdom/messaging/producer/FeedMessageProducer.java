package cobook.buddywisdom.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import cobook.buddywisdom.feed.dto.ScheduleEventDto;

@Component
public class FeedMessageProducer {

	private final RabbitTemplate rabbitTemplate;

	private static final String EXCHANGE = "budsdom.events";
	private static final String SCHEDULE_ROUTING_KEY = "schedule.events";

	public FeedMessageProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void produceScheduleEvent(ScheduleEventDto scheduleEventDto) {
		rabbitTemplate.convertAndSend(EXCHANGE, SCHEDULE_ROUTING_KEY, scheduleEventDto);
	}


}
