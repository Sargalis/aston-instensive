package org.example.task6;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity DTO")
public class UserDTO extends RepresentationModel<UserDTO> {

	@Schema(description = "User ID")
	private Long id;

	@Schema(description = "User name")
	private String name;

	@Schema(description = "User email address")
	private String email;

	@Schema(description = "User age")
	private Integer age;
}