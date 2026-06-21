package org.example;

import java.util.List;

public class UserService {
	private final UserDAO userDAO;

	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserEntity createUser(String name, String email, Integer age) {
		// Простая валидация
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Имя не может быть пустым");
		}
		if (email == null || !email.contains("@")) {
			throw new IllegalArgumentException("Некорректный email");
		}
		if (age != null && (age < 0 || age > 150)) {
			throw new IllegalArgumentException("Некорректный возраст");
		}

		UserEntity user = new UserEntity(name, email, age);
		return userDAO.create(user);
	}

	public UserEntity getUserById(Long id) {
		return userDAO.read(id);
	}

	public List<UserEntity> getAllUsers() {
		return userDAO.readAll();
	}

	public UserEntity updateUser(Long id, String name, String email, Integer age) {
		UserEntity user = userDAO.read(id);
		if (user == null) {
			throw new RuntimeException("Пользователь с ID " + id + " не найден");
		}
		user.setName(name);
		user.setEmail(email);
		user.setAge(age);
		return userDAO.update(user);
	}

	public void deleteUser(Long id) {
		userDAO.delete(id);
	}

}