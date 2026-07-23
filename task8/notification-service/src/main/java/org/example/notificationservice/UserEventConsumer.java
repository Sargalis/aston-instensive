package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventConsumer {

	@Autowired
	private NotificationService notificationService;

	@KafkaListener(topics = "user-events", groupId = "notification-group")
	public void consumeUserEvent(UserEvent event) {
		log.info("Получено событие из Kafka: {}", event);

		if ("USER_CREATED".equals(event.getEventType())) {
			notificationService.sendUserCreatedNotification(event.getEmail(), event.getName());
			log.info("Уведомление о создании отправлено для: {}", event.getEmail());
		} else if ("USER_DELETED".equals(event.getEventType())) {
			notificationService.sendUserDeletedNotification(event.getEmail(), event.getName());
			log.info("Уведомление об удалении отправлено для: {}", event.getEmail());
		}
	}
}