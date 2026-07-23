package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public void sendEmail(String to, String subject, String message) {
		log.info("   Отправка email:");
		log.info("   Кому: {}", to);
		log.info("   Тема: {}", subject);
		log.info("   Сообщение: {}", message);
		log.info("   Email отправлен!");

		NotificationEntity notification = new NotificationEntity();
		notification.setUserId(0L);
		notification.setMessage("Email отправлен на " + to + ": " + subject);
		notification.setType("EMAIL");
		notificationRepository.save(notification);
	}

	public void sendUserCreatedNotification(String email, String name) {
		String subject = "Создание";
		String message = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
		sendEmail(email, subject, message);

		NotificationEntity notification = new NotificationEntity();
		notification.setUserId(0L);
		notification.setMessage("Уведомление о создании отправлено на " + email);
		notification.setType("SYSTEM");
		notificationRepository.save(notification);
	}

	public void sendUserDeletedNotification(String email, String name) {
		String subject = "Удаление";
		String message = "Здравствуйте! Ваш аккаунт был удалён.";
		sendEmail(email, subject, message);

		NotificationEntity notification = new NotificationEntity();
		notification.setUserId(0L);
		notification.setMessage("Уведомление об удалении отправлено на " + email);
		notification.setType("SYSTEM");
		notificationRepository.save(notification);
	}

	public List<NotificationDTO> getNotificationsByUser(Long userId) {
		return notificationRepository.findByUserId(userId).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public List<NotificationDTO> getAllNotifications() {
		return notificationRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	private NotificationDTO toDTO(NotificationEntity entity) {
		return new NotificationDTO(
				entity.getId(),
				entity.getUserId(),
				entity.getMessage(),
				entity.getType(),
				entity.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
	}
}