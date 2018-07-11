package de.sonnmatt.muutos;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.sonnmatt.muutos.jpa.SystemParamameterJPA;
import de.sonnmatt.muutos.jpa.SystemParamameterJPA.SystemParameterCodes;

public class SystemManagement {
	private static Logger log = LogManager.getLogger(SystemManagement.class);
	private static EntityManager entityManager = HibernateUtil.getEntityManager();
	private static SystemManagement instance;

	private HashMap<String, String> paramMap = new HashMap<>();

	private SystemManagement() {
		log.traceEntry("SystemManagement()");
		List<SystemParamameterJPA> params = null;
		params = entityManager	.createNamedQuery(SystemParamameterJPA.GetAll, SystemParamameterJPA.class)
								.getResultList();
		log.trace("params.seize(): {}; params.isEmpty(): {}", params.size(), params.isEmpty());
		if (params.isEmpty()) {
			log.trace("Init starting");
			InitializeSetup init = new InitializeSetup();
			init.setupSystem();
			DateTimeFormatter dtf = DateTimeFormatter.ISO_INSTANT;
			entityManager.getTransaction().begin();
			SystemParamameterJPA sysParam = new SystemParamameterJPA();
			sysParam.setCode(SystemParameterCodes.Initialized);
			sysParam.setValue(ZonedDateTime.now().format(dtf));
			entityManager.persist(sysParam);
			entityManager.flush();
			entityManager.getTransaction().commit();
			log.trace("Init done");
			params = entityManager	.createNamedQuery(SystemParamameterJPA.GetAll, SystemParamameterJPA.class)
									.getResultList();
			log.trace("params.seize(): {}; params.isEmpty(): {}", params.size(), params.isEmpty());
		}
		params.forEach(p -> paramMap.put(p.getCode(), p.getValue()));
	}

	public static SystemManagement getInstance() {
		if (instance == null) {
			synchronized (SystemManagement.class) {
				if (instance == null) {
					instance = new SystemManagement();
				}
			}
		}
		return instance;
	}

	public String getSystemParameter(SystemParameterCodes code) {
		return paramMap.get(code.toString());
	}
}
