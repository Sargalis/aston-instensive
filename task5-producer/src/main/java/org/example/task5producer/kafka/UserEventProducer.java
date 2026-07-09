package org.example.task5producer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.task5producer.dto.UserEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventProducer {

	private static final String TOPIC = "user-events";

	@Autowired
	private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

	public void sendUserEvent(String email, String eventType) {
		UserEventDTO event = new UserEventDTO(email, eventType);
		kafkaTemplate.send(TOPIC, email, event);
		log.info("Новый ивент Kafka: type={}, email={}", event.getEventType(), event.getEmail());
	}
}