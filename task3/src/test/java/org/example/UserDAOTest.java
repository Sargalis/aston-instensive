package org.example;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDAOTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	private static UserDAO userDAO;

	@BeforeAll
	static void setUp() {
		// Настройка для Podman
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("DOCKER_HOST", "npipe:////./pipe/docker_engine");
			System.setProperty("TESTCONTAINERS_RYUK_DISABLED", "true");
		}

		System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
		System.setProperty("hibernate.connection.username", postgres.getUsername());
		System.setProperty("hibernate.connection.password", postgres.getPassword());

		HibernateUtil.rebuildSessionFactory();
		userDAO = new UserDAO();
	}

	@AfterAll
	static void tearDown() {
		HibernateUtil.shutdown();
	}

	@BeforeEach
	void cleanUp() {
		userDAO.readAll().forEach(u -> userDAO.delete(u.getId()));
	}

	@Test
	void create_ShouldSaveUser() {
		User user = new User("John", "john@test.com", 25);
		User saved = userDAO.create(user);
		assertNotNull(saved.getId());
		assertEquals("John", saved.getName());
	}

	@Test
	void read_ShouldFindUser() {
		User saved = userDAO.create(new User("John", "john@test.com", 25));
		User found = userDAO.read(saved.getId());
		assertNotNull(found);
		assertEquals("john@test.com", found.getEmail());
	}

	@Test
	void read_NotFound_ShouldReturnNull() {
		User found = userDAO.read(999L);
		assertNull(found);
	}

	@Test
	void readAll_ShouldReturnAll() {
		userDAO.create(new User("User1", "u1@test.com", 20));
		userDAO.create(new User("User2", "u2@test.com", 25));
		assertEquals(2, userDAO.readAll().size());
	}

	@Test
	void readAll_EmptyTable_ShouldReturnEmptyList() {
		assertTrue(userDAO.readAll().isEmpty());
	}

	@Test
	void update_ShouldUpdateUser() {
		User saved = userDAO.create(new User("John", "john@test.com", 25));
		saved.setName("Johnny");
		User updated = userDAO.update(saved);
		assertEquals("Johnny", updated.getName());
		User found = userDAO.read(saved.getId());
		assertEquals("Johnny", found.getName());
	}

	@Test
	void delete_ShouldDeleteUser() {
		User saved = userDAO.create(new User("John", "john@test.com", 25));
		userDAO.delete(saved.getId());
		assertNull(userDAO.read(saved.getId()));
	}
}