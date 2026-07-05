package org.example.task5consumer.controller;

import org.example.task5producer.dto.UserEventDTO;
import org.example.task5consumer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/send")
	public ResponseEntity<String> sendNotification(@RequestBody UserEventDTO request) {
		if ("CREATED".equals(request.getEventType())) {
			emailService.sendCreationEmail(request.getEmail());
			return ResponseEntity.ok("Письмо о создании отправлено на " + request.getEmail());
		} else if ("DELETED".equals(request.getEventType())) {
			emailService.sendDeletionEmail(request.getEmail());
			return ResponseEntity.ok("Письмо об удалении отправлено на " + request.getEmail());
		} else {
			return ResponseEntity.badRequest().body("Неизвестный тип события: " + request.getEventType());
		}
	}
}

/*
curl -X POST http://localhost:8081/api/notifications/send \
	-H "Content-Type: application/json" \
	-d '{"email":"johnPork@test.com","eventType":"CREATED"}'

curl -X POST http://localhost:8081/api/notifications/send \
	-H "Content-Type: application/json" \
	-d '{"email":"johnPork@test.com","eventType":"DELETED"}'
*/