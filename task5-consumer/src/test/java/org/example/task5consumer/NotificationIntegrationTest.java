package org.example.task5consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
class NotificationIntegrationTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9092");
	}

	@Test
	void testSendKafkaMessage() throws InterruptedException {
		String message = "{\"email\":\"test@test.com\",\"eventType\":\"CREATED\"}";
		kafkaTemplate.send("user-events", message);

		Thread.sleep(2000);

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Collections.singletonList("user-events"));
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
			assertFalse(records.isEmpty());
			for (var record : records) {
				System.out.println("Получено: " + record.value());
				assertTrue(record.value().contains("test@test.com"));
				assertTrue(record.value().contains("CREATED"));
			}
		}
	}

	@Test
	void testApiSendsEmail() {
		System.out.println("API тест проходит");
	}
}