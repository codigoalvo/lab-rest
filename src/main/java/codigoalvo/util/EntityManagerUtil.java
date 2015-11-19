package codigoalvo.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class EntityManagerUtil implements ServletContextListener {

	private static EntityManagerFactory emf;
	private static EntityManager em;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		emf = Persistence.createEntityManagerFactory("default");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}
	}

	private static EntityManager createEntityManager() {
		if (emf == null) {
			throw new IllegalStateException("Context is not initialized yet.");
		}
		return emf.createEntityManager();
	}

	public static EntityManager getEntityManager() {
		if (em == null) {
			em = createEntityManager();
		}
		return em;
	}

}
