package org.example.task5producer;

import org.example.task5producer.dto.UserEventDTO;
import org.example.task5producer.kafka.UserEventProducer;
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
	private UserEventProducer userEventProducer;

	public User createUser(User user) {
		User saved = userRepository.save(user);
		userEventProducer.sendUserEvent(saved.getEmail(), "CREATED");
		return saved;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User updateUser(Long id, User userData) {
		User user = getUserById(id);
		user.setName(userData.getName());
		user.setEmail(userData.getEmail());
		user.setAge(userData.getAge());
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		User user = getUserById(id);
		String email = user.getEmail();
		userRepository.deleteById(id);
		userEventProducer.sendUserEvent(email, "DELETED");
	}

	public UserDTO toDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setAge(user.getAge());
		return dto;
	}
}