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
	static Logger log = LogManager.getLogger(HibernateUtil.class);
	static String classname = "HibernateUtil";
	
	private static final EntityManagerFactory emFactory;
	static {
		log.traceEntry("{}.EntityManagerFactory", classname);
		emFactory = Persistence.createEntityManagerFactory("muutos");
		log.entry("{}.EntityManagerFactory: done", classname);
	}

	public static EntityManager getEntityManager() {
		log.traceEntry("{}.getEntityManager()", classname);
		if (emFactory == null) {
			log.error("{}.getEntityManager(): emFactor is null", classname);
		}
		EntityManager entityMan = emFactory.createEntityManager();
		log.entry("{}.getEntityManager(): entityMan created", classname);
		return entityMan;
	}

	public static void close() {
		emFactory.close();
	}
}
