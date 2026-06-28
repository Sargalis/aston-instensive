package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserEntity createUser(String name, String email, Integer age) {
		UserEntity user = new UserEntity();
		user.setName(name);
		user.setEmail(email);
		user.setAge(age);
		return userRepository.save(user);
	}

	public UserEntity getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	public UserEntity updateUser(Long id, String name, String email, Integer age) {
		UserEntity user = getUserById(id);
		user.setName(name);
		user.setEmail(email);
		user.setAge(age);
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}