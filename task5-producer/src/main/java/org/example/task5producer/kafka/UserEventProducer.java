package org.example.task5producer.kafka;

import org.example.task5producer.dto.UserEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

	private static final String TOPIC = "user-events";

	@Autowired
	private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

	public void sendUserEvent(String email, String eventType) {
		UserEventDTO event = new UserEventDTO(email, eventType);
		kafkaTemplate.send(TOPIC, email, event);
		System.out.println("Новый ивент Kafka: " + event);
	}
}