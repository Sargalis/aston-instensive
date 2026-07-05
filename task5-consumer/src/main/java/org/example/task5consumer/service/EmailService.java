package org.example.task5consumer.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	public void sendEmail(String to, String subject, String body) {
		System.out.println("==================================");
		System.out.println("TO: " + to);
		System.out.println("SUBJECT: " + subject);
		System.out.println("BODY:" + body);
		System.out.println("==================================");
	}

	public void sendCreationEmail(String email) {
		sendEmail(email, "Создание", "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."); // Очепятка в задании
	}

	public void sendDeletionEmail(String email) {
		sendEmail(email, "Удаление", "Здравствуйте! Ваш аккаунт был удалён.");
	}
}