package org.example.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private static final String TOPIC = "user-events";

	public void sendUserCreatedEvent(UserEvent event) {
		event.setEventType("USER_CREATED");
		event.setTimestamp(System.currentTimeMillis());
		kafkaTemplate.send(TOPIC, String.valueOf(event.getUserId()), event);
		log.info("Отправлено событие создания в Kafka: {}", event);
	}

	public void sendUserDeletedEvent(UserEvent event) {
		event.setEventType("USER_DELETED");
		event.setTimestamp(System.currentTimeMillis());
		kafkaTemplate.send(TOPIC, String.valueOf(event.getUserId()), event);
		log.info("Отправлено событие удаления в Kafka: {}", event);
	}
}