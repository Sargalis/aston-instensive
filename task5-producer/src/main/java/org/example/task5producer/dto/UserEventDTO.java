package org.example.task5producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
	private String email;
	private String eventType; // CREATED и DELETED
}