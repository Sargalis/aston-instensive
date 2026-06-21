package org.example;

import java.util.Scanner;
import java.util.List;

public class Main {
	private static final UserService userService = new UserService(new UserDAO());
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("=== User Service ===");

		while (true) {
			System.out.println("\n1. Создать");
			System.out.println("2. Найти по ID");
			System.out.println("3. Все пользователи");
			System.out.println("4. Обновить");
			System.out.println("5. Удалить");
			System.out.println("0. Выход");
			System.out.print("Выбор: ");

			String choice = scanner.nextLine();

			switch (choice) {
				case "1": create(); break;
				case "2": read(); break;
				case "3": readAll(); break;
				case "4": update(); break;
				case "5": delete(); break;
				case "0":
					HibernateUtil.shutdown();
					System.out.println("До свидания!");
					return;
				default: System.out.println("Неверный выбор");
			}
		}
	}

	private static void create() {
		System.out.print("Имя: ");
		String name = scanner.nextLine();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Возраст: ");
		String ageStr = scanner.nextLine();
		Integer age = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);

		try {
			UserEntity user = userService.createUser(name, email, age);
			System.out.println("Создан! ID: " + user.getId());
		} catch (Exception e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private static void read() {
		System.out.print("ID: ");
		Long id = Long.parseLong(scanner.nextLine());
		UserEntity user = userService.getUserById(id);
		if (user != null) {
			System.out.println(user);
		} else {
			System.out.println("Пользователь не найден");
		}
	}

	private static void readAll() {
		List<UserEntity> users = userService.getAllUsers();
		if (users.isEmpty()) {
			System.out.println("Нет пользователей");
		} else {
			users.forEach(System.out::println);
		}
	}

	private static void update() {
		System.out.print("ID для обновления: ");
		Long id = Long.parseLong(scanner.nextLine());
		UserEntity user = userService.getUserById(id);
		if (user == null) {
			System.out.println("Пользователь не найден");
			return;
		}

		System.out.print("Новое имя (Enter - оставить): ");
		String name = scanner.nextLine();
		if (!name.isEmpty()) user.setName(name);

		System.out.print("Новый email (Enter - оставить): ");
		String email = scanner.nextLine();
		if (!email.isEmpty()) user.setEmail(email);

		System.out.print("Новый возраст (Enter - оставить): ");
		String ageStr = scanner.nextLine();
		if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

		try {
			UserEntity updated = userService.updateUser(id, user.getName(), user.getEmail(), user.getAge());
			System.out.println("Обновлено!");
			System.out.println(updated);
		} catch (Exception e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}

	private static void delete() {
		System.out.print("ID для удаления: ");
		Long id = Long.parseLong(scanner.nextLine());
		System.out.print("Удалить? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
			try {
				userService.deleteUser(id);
				System.out.println("Удалено!");
			} catch (Exception e) {
				System.out.println("Ошибка: " + e.getMessage());
			}
		}
	}
}