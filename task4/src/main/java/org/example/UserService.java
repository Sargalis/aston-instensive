package org.example;

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

	public UserDTO createUser(UserDTO request) {
		UserEntity user = new UserEntity();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setAge(request.getAge());
		UserEntity saved = userRepository.save(user);
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
		return toDTO(userRepository.save(user));
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	private UserDTO toDTO(UserEntity user) {
		return new UserDTO(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getAge()
		);
	}
}