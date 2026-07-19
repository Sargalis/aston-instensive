package org.example.gatewayservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@GetMapping("/users")
	public Mono<Map<String, String>> fallbackUsers() {
		Map<String, String> response = new HashMap<>();
		response.put("status", "SERVICE_UNAVAILABLE");
		response.put("message", "Сервис временно недоступен. Попробуйте позже.");
		response.put("timestamp", String.valueOf(System.currentTimeMillis()));
		return Mono.just(response);
	}
}