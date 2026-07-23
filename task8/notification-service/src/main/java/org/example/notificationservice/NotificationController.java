package org.example.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	// Получить все уведомления
	@GetMapping
	public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
		return ResponseEntity.ok(notificationService.getAllNotifications());
	}

	// Получить уведомления по ID пользователя
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
	}

	// API для отправки email вручную
	@PostMapping("/send-email")
	public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
		notificationService.sendEmail(request.getTo(), request.getSubject(), request.getMessage());
		return ResponseEntity.ok("Email отправлен успешно!");
	}

	// API для отправки приветственного email
	@PostMapping("/send-welcome")
	public ResponseEntity<String> sendWelcomeEmail(@RequestParam String email, @RequestParam String name) {
		notificationService.sendUserCreatedNotification(email, name);
		return ResponseEntity.ok("Письмо о создании отправлено на " + email);
	}

	// API для отправки email об удалении
	@PostMapping("/send-deletion")
	public ResponseEntity<String> sendDeletionEmail(@RequestParam String email, @RequestParam String name) {
		notificationService.sendUserDeletedNotification(email, name);
		return ResponseEntity.ok("Письмо об удалении отправлено на " + email);
	}
}