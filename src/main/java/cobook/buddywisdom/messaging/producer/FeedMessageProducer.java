package cobook.buddywisdom.messaging.producer;

import java.util.function.Supplier;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import cobook.buddywisdom.feed.dto.ScheduleEventDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedMessageProducer {

	private final RabbitTemplate rabbitTemplate;

	private static final String EXCHANGE = "budsdom.events";
	private static final String SCHEDULE_ROUTING_KEY = "schedule.events";

	public void produceScheduleEvent(Long senderId, Long receiverId, Supplier<String> messageSupplier) {
		rabbitTemplate.convertAndSend(EXCHANGE, SCHEDULE_ROUTING_KEY, ScheduleEventDto.builder()
			.senderId(senderId)
			.receiverId(receiverId)
			.feedMessage(messageSupplier.get())
			.build()
		);
	}
}
