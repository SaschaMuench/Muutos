package de.sonnmatt.muutos;

import java.util.List;

import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.sonnmatt.muutos.exceptions.LoginException;
import de.sonnmatt.muutos.exceptions.LoginException.LoginExceptionType;
import de.sonnmatt.muutos.jpa.UserJPA;
import static de.sonnmatt.muutos.jpa.UserJPA.*;

public class UserService {
	private static Logger log = LogManager.getLogger(UserService.class);
	private static Logger userLog = LogManager.getLogger("UserLogin");
	private static EntityManager entityManager = HibernateUtil.getEntityManager();
	private String tenantID = null;

	public boolean verifyUser(String username, String password) throws LoginException {
		log.traceEntry("verifyUser({}, pwd)", username);

		// ThreadContext.put("client.sessionid", session.getId());
		// log.trace("verifyUser() client.sessionid set");

		List<UserJPA> user = entityManager	.createNamedQuery(GetUserByLoginAndTenantId, UserJPA.class)
											.setParameter(ParamUserLogin, username)
											.setParameter(ParamUserTenantId, tenantID)
											.getResultList();
		log.trace("verifyUser() List<UserJPA> generated. Size: {}", user.size());

		if (user.size() == 0) {
			userLog.trace("Unknown user: {}", username);
			throw new LoginException(username, LoginExceptionType.userUnknown);
		}
		user.clear();
		user = entityManager.createNamedQuery(GetUserByLoginAndTenantIdAndActive, UserJPA.class)
							.setParameter(ParamUserLogin, username)
							.setParameter(ParamUserTenantId, tenantID)
							.setParameter(ParamUserActive, true)
							.getResultList();
		if (user.size() == 0) {
			userLog.trace("Login is locked: {}", username);
			throw new LoginException(username, LoginExceptionType.loginLocked);
		}
		if (user.size() > 1) {
			userLog.trace("Too many active users: {}", username);
			throw new LoginException(username, LoginExceptionType.tooManyUsers);
		}
		if (!user.get(0).checkPassword(password)) {
			userLog.trace("Wrong password: {}", username);
			throw new LoginException("", LoginExceptionType.passwordWrong);
		}
		userLog.traceExit("User verified: {}", username);
		return true;
	}

	public String getTenantID() {
		return tenantID;
	}

	public UserService setTenantID(String tenantID) {
		log.traceEntry("setTenantID({})", tenantID);
		this.tenantID = tenantID;
		return this;
	}

	public UserJPA getUser(String username) {
		log.traceEntry("getUser({})", username);

		List<UserJPA> user = entityManager	.createNamedQuery(GetUserByLoginAndTenantIdAndActive, UserJPA.class)
											.setParameter(ParamUserLogin, username)
											.setParameter(ParamUserTenantId, tenantID)
											.setParameter(ParamUserActive, true)
											.getResultList();
		return user.get(0);
	}
}
