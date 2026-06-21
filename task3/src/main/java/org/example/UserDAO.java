package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UserDAO {
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

	// CREATE
	public UserEntity create(UserEntity user) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			session.persist(user);
			tx.commit();
			log.info("Создан пользователь: {}", user.getEmail());
			return user;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			log.error("Ошибка создания", e);
			throw new RuntimeException("Не удалось создать пользователя", e);
		}
	}

	// READ (по id)
	public UserEntity read(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(UserEntity.class, id);
		}
	}

	// READ (все)
	public List<UserEntity> readAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM UserEntity", UserEntity.class).list();
		}
	}

	// UPDATE
	public UserEntity update(UserEntity user) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			UserEntity merged = session.merge(user);
			tx.commit();
			log.info("Обновлен пользователь: {}", user.getEmail());
			return merged;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			log.error("Ошибка обновления", e);
			throw new RuntimeException("Не удалось обновить пользователя", e);
		}
	}

	// DELETE
	public void delete(Long id) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			UserEntity user = session.get(UserEntity.class, id);
			if (user != null) {
				session.remove(user);
				log.info("Удален пользователь с ID: {}", id);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			log.error("Ошибка удаления", e);
			throw new RuntimeException("Не удалось удалить пользователя", e);
		}
	}
}