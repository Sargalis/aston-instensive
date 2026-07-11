package org.example.task6;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	@Operation(summary = "Create new User", description = "Creates new User entity in database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User successfully created"),
			@ApiResponse(responseCode = "400", description = "Invalid input/data")
	})
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO request) {
		UserDTO response = userService.createUser(request);
		addHateoasLinks(response);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get User by ID", description = "Returns the User by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<UserDTO> getUserById(
			@Parameter(description = "User ID")
			@PathVariable Long id) {
		UserDTO response = userService.getUserById(id);
		addHateoasLinks(response);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@Operation(summary = "Get all Users", description = "Returns a list of all Users")
	@ApiResponse(responseCode = "200", description = "User list")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> responses = userService.getAllUsers();
		responses.forEach(this::addHateoasLinks);
		return ResponseEntity.ok(responses);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update User", description = "Updates existing User data")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated successfully"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "400", description = "Invalid data/input")
	})
	public ResponseEntity<UserDTO> updateUser(
			@Parameter(description = "ID пользователя", example = "1")
			@PathVariable Long id,
			@RequestBody UserDTO request) {
		UserDTO response = userService.updateUser(id, request);
		addHateoasLinks(response);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete User", description = "Deletes User by specified ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "User deleted"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<Void> deleteUser(
			@Parameter(description = "User ID")
			@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	private void addHateoasLinks(UserDTO dto) {
		if (dto.getId() == null) {
			return;
		}

		Link selfLink = linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel();
		dto.add(selfLink);

		Link allLink = linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users");
		dto.add(allLink);

		Link updateLink = linkTo(methodOn(UserController.class).updateUser(dto.getId(), null)).withRel("update");
		dto.add(updateLink);

		Link deleteLink = linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("delete");
		dto.add(deleteLink);
	}
}