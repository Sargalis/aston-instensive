package org.example.task5producer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private String name;
	private String email;
	private Integer age;
}