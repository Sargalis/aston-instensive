package org.example;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserEntityDAOTest {

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
		UserEntity user = new UserEntity("John", "john@test.com", 25);
		UserEntity saved = userDAO.create(user);
		assertNotNull(saved.getId());
		assertEquals("John", saved.getName());
	}

	@Test
	void read_ShouldFindUser() {
		UserEntity saved = userDAO.create(new UserEntity("John", "john@test.com", 25));
		UserEntity found = userDAO.read(saved.getId());
		assertNotNull(found);
		assertEquals("john@test.com", found.getEmail());
	}

	@Test
	void read_NotFound_ShouldReturnNull() {
		UserEntity found = userDAO.read(999L);
		assertNull(found);
	}

	@Test
	void readAll_ShouldReturnAll() {
		userDAO.create(new UserEntity("User1", "u1@test.com", 20));
		userDAO.create(new UserEntity("User2", "u2@test.com", 25));
		assertEquals(2, userDAO.readAll().size());
	}

	@Test
	void readAll_EmptyTable_ShouldReturnEmptyList() {
		assertTrue(userDAO.readAll().isEmpty());
	}

	@Test
	void update_ShouldUpdateUser() {
		UserEntity saved = userDAO.create(new UserEntity("John", "john@test.com", 25));
		saved.setName("Johnny");
		UserEntity updated = userDAO.update(saved);
		assertEquals("Johnny", updated.getName());
		UserEntity found = userDAO.read(saved.getId());
		assertEquals("Johnny", found.getName());
	}

	@Test
	void delete_ShouldDeleteUser() {
		UserEntity saved = userDAO.create(new UserEntity("John", "john@test.com", 25));
		userDAO.delete(saved.getId());
		assertNull(userDAO.read(saved.getId()));
	}
}