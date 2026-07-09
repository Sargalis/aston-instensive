package org.example.task5consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.task5producer.dto.UserEventDTO;
import org.example.task5consumer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventConsumer {

	@Autowired
	private EmailService emailService;

	@KafkaListener(topics = "user-events", groupId = "notification-group")
	public void consume(UserEventDTO event) {
		log.info("Получено событие из Kafka: {}", event);

		if ("CREATED".equals(event.getEventType())) {
			emailService.sendCreationEmail(event.getEmail());
		} else if ("DELETED".equals(event.getEventType())) {
			emailService.sendDeletionEmail(event.getEmail());
		} else {
			log.error("Неизвестный тип события: {}", event.getEventType());
		}
	}
}