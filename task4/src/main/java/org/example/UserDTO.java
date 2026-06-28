package org.example;

import lombok.*;

@Data
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private String name;
	private String email;
	private Integer age;
}