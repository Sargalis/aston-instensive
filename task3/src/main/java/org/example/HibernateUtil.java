package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
	private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;

	static {
		initSessionFactory();
	}

	private static void initSessionFactory() {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			log.info("SessionFactory инициализирована");
		} catch (Exception e) {
			log.error("Ошибка инициализации Hibernate", e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void rebuildSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
		initSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}