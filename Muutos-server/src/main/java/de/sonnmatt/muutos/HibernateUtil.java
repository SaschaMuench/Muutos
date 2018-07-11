/**
 * 
 */
package de.sonnmatt.muutos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author MuenSasc
 *
 */
public class HibernateUtil {
	static Logger log = LogManager.getLogger("HibernateUtil.class");
	static String classname = "HibernateUtil";
	
	private static final EntityManagerFactory emFactory;
	static {
		emFactory = Persistence.createEntityManagerFactory("muutos");
		log.trace("{}.EntityManagerFactory: done", classname);
	}

	public static EntityManager getEntityManager() {
		log.traceEntry("{}.getEntityManager()", classname);
		if (emFactory == null) {
			log.error("{}.getEntityManager(): emFactor is null", classname);
		}
		EntityManager entityMan = emFactory.createEntityManager();
		log.trace("{}.getEntityManager(): entityMan created", classname);
		return entityMan;
	}

	public static void close() {
		log.trace("{}.close()", classname);
		emFactory.close();
	}

}

