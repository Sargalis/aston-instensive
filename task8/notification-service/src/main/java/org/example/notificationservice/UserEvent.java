package org.example.notificationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
	private Long userId;
	private String email;
	private String name;
	private String eventType;
	private Long timestamp;
}