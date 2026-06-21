package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

	@Mock
	private UserDAO userDAO;

	@InjectMocks
	private UserService userService;

	@Test
	void createUser_ShouldCallDAOCreate() {
		UserEntity user = new UserEntity("John", "john@test.com", 25);
		when(userDAO.create(any(UserEntity.class))).thenReturn(user);

		UserEntity result = userService.createUser("John", "john@test.com", 25);

		assertNotNull(result);
		verify(userDAO).create(any(UserEntity.class));
	}

	@Test
	void createUser_InvalidName_ShouldThrowException() {
		assertThrows(IllegalArgumentException.class,
				() -> userService.createUser("", "john@test.com", 25));
		verify(userDAO, never()).create(any(UserEntity.class));
	}

	@Test
	void createUser_InvalidEmail_ShouldThrowException() {
		assertThrows(IllegalArgumentException.class,
				() -> userService.createUser("John", "invalid", 25));
		verify(userDAO, never()).create(any(UserEntity.class));
	}

	@Test
	void getUserById_ShouldCallDAORead() {
		// Подготовка
		UserEntity user = new UserEntity("John", "john@test.com", 25);
		when(userDAO.read(1L)).thenReturn(user);

		// Действие
		UserEntity result = userService.getUserById(1L);

		// Проверка
		assertNotNull(result);
		verify(userDAO).read(1L);
	}

	@Test
	void getUserById_NotFound_ShouldReturnNull() {
		when(userDAO.read(999L)).thenReturn(null);
		UserEntity result = userService.getUserById(999L);
		assertNull(result);
	}

	@Test
	void getAllUsers_ShouldCallDAOReadAll() {
		List<UserEntity> users = Arrays.asList(
				new UserEntity("John", "john@test.com", 25),
				new UserEntity("Mary", "mary@test.com", 30)
		);
		when(userDAO.readAll()).thenReturn(users);

		List<UserEntity> result = userService.getAllUsers();
		assertEquals(2, result.size());
		verify(userDAO).readAll();
	}

	@Test
	void updateUser_ShouldCallDAOUpdate() {
		UserEntity existing = new UserEntity("John", "john@test.com", 25);
		existing.setId(1L);
		when(userDAO.read(1L)).thenReturn(existing);
		when(userDAO.update(any(UserEntity.class))).thenReturn(existing);

		UserEntity result = userService.updateUser(1L, "Johnny", "johnny@test.com", 26);
		assertEquals("Johnny", result.getName());
		verify(userDAO).update(any(UserEntity.class));
	}

	@Test
	void updateUser_NotFound_ShouldThrowException() {
		when(userDAO.read(999L)).thenReturn(null);
		assertThrows(RuntimeException.class,
				() -> userService.updateUser(999L, "John", "john@test.com", 25));
		verify(userDAO, never()).update(any(UserEntity.class));
	}

	@Test
	void deleteUser_ShouldCallDAODelete() {
		userService.deleteUser(1L);
		verify(userDAO).delete(1L);
	}

}