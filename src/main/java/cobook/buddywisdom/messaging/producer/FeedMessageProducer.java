package cobook.buddywisdom.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import cobook.buddywisdom.feed.dto.ScheduleEventDto;

import java.util.function.Supplier;

@Component
public class FeedMessageProducer {

	private final RabbitTemplate rabbitTemplate;

	private static final String EXCHANGE = "budsdom.events";
	private static final String SCHEDULE_ROUTING_KEY = "schedule.events";

	public FeedMessageProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void produceScheduleEvent(long senderId, long receiverId, Supplier<String> messageSupplier) {
		rabbitTemplate.convertAndSend(EXCHANGE, SCHEDULE_ROUTING_KEY, ScheduleEventDto.builder()
				.senderId(senderId)
				.receiverId(receiverId)
				.feedMessage(messageSupplier.get())
				.build()
		);
	}
}
