package org.example.task5consumer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@Test
	void testSendCreationEmail() {
		assertDoesNotThrow(() -> emailService.sendCreationEmail("test@test.com"));
	}

	@Test
	void testSendDeletionEmail() {
		assertDoesNotThrow(() -> emailService.sendDeletionEmail("test@test.com"));
	}
}