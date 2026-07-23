package org.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEventProducer eventProducer;

	public UserDTO createUser(UserDTO request) {
		UserEntity user = new UserEntity();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setAge(request.getAge());
		UserEntity saved = userRepository.save(user);

		UserEvent event = new UserEvent(
				saved.getId(),
				saved.getEmail(),
				saved.getName(),
				"USER_CREATED",
				System.currentTimeMillis()
		);
		eventProducer.sendUserCreatedEvent(event);

		return toDTO(saved);
	}

	public UserDTO getUserById(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		return toDTO(user);
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public UserDTO updateUser(Long id, UserDTO request) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setAge(request.getAge());
		UserEntity updated = userRepository.save(user);
		return toDTO(updated);
	}

	public void deleteUser(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		String email = user.getEmail();
		String name = user.getName();

		userRepository.deleteById(id);

		UserEvent event = new UserEvent(
				id,
				email,
				name,
				"USER_DELETED",
				System.currentTimeMillis()
		);
		eventProducer.sendUserDeletedEvent(event);
	}

	private UserDTO toDTO(UserEntity user) {
		return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge());
	}
}