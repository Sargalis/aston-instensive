package org.example.task5consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

	public void sendEmail(String to, String subject, String body) {
		log.info("==================================");
		log.info("TO: {}", to);
		log.info("SUBJECT: {}", subject);
		log.info("BODY:{}", body);
		log.info("==================================");
	}

	public void sendCreationEmail(String email) {
		sendEmail(email, "Создание", "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."); // Очепятка в задании
	}

	public void sendDeletionEmail(String email) {
		sendEmail(email, "Удаление", "Здравствуйте! Ваш аккаунт был удалён.");
	}
}