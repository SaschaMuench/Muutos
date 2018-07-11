/**
 * 
 */
package de.sonnmatt.muutos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.exceptions.GeneralException;
import de.sonnmatt.muutos.exceptions.GeneralException.GeneralExceptionType;
import de.sonnmatt.muutos.exceptions.LoginException;
import de.sonnmatt.muutos.exceptions.LoginException.LoginExceptionType;
import de.sonnmatt.muutos.jpa.TenantJPA;
import de.sonnmatt.muutos.jpa.UserJPA;
import de.sonnmatt.muutos.rpc.LoginService;
import static de.sonnmatt.muutos.SessionAttributes.*;

/**
 * @author MuenSasc
 *
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = -8424967452520912846L;
	private static Logger log = LogManager.getLogger(LoginServiceImpl.class);
	private static EntityManager entityManager = HibernateUtil.getEntityManager();
	private HttpSession userSession = null;

	public LoginServiceImpl() {
		super();
	}

	public LoginServiceImpl(Object delegate) {
		super(delegate);
	}

	@Override
	public TextResourcesDTO getText(String tenantUrl, String language, String action) throws LoginException {
		log.traceEntry("getText({}, {}, {})", tenantUrl, language, action);

		SystemManagement sysMgnmt = SystemManagement.getInstance();

		List<TenantJPA> tenantJPA = entityManager	.createNamedQuery(TenantJPA.GetTenantByUrl, TenantJPA.class)
													.setParameter(TenantJPA.ParamTenantUrl, tenantUrl)
													.getResultList();
		String tenantID = tenantJPA.get(0).getId();
		log.debug("getText(): tenantJPA.size() from Url {} found: {}", tenantUrl, tenantJPA.size());
		log.debug("getText(): tenantID from Url {} found: {}", tenantUrl, tenantID);
		
		userSession = this.getThreadLocalRequest().getSession(true);
		synchronized (userSession) {
			userSession.setAttribute(SESSATTR_TENANT_ID, tenantID);
			userSession.setAttribute(SESSATTR_LANGUAGE, language);
			userSession.setAttribute(SESSATTR_PARAMS, sysMgnmt);
		}

		TextService textServ = new TextService();
		try {
			return textServ.setLanguage(language).setTenantByUrl(tenantUrl).getTexts(action);
		} catch (GeneralException e) {
			if (e.getType() == GeneralExceptionType.unknownLanguage) {
				log.error("getText({}, {}): unknown language", language, action.toString());
				throw (new LoginException("Unknown language: " + language, LoginExceptionType.generalError));
			}
			if (e.getType() == GeneralExceptionType.unknownTenant) {
				log.error("getText({}, {}): unknown tenant", language, action.toString());
				throw (new LoginException("Unknown tenant: " + language, LoginExceptionType.generalError));
			}
		}
		return null;
	}

	@Override
	public UserDTO loginUser(String username, String password) throws LoginException {
		log.traceEntry("loginUser({}, pwd)", username);

		UserService userService = new UserService();
		HttpSession userSession = this.getThreadLocalRequest().getSession(true);
		try {
			String tenantId = (String)userSession.getAttribute(SESSATTR_TENANT_ID);
			if (userService.setTenantID(tenantId).verifyUser(username, password)) {
				log.trace("loginUser({}, pwd): verified in with Session ID {}", username, userSession.getId());
				UserJPA user = userService.getUser(username);
				UserDTO userDto = user.generateDTO();
				userDto.set(UserFields.SessionID, userSession.getId());
				
				synchronized (userSession) {
					userSession.setAttribute(SESSATTR_LOGIN, username);
					userSession.setAttribute(SESSATTR_USER, userDto);
				};
				return log.traceExit("loginUser(" + username + ", pwd) succeded: {}", userDto);
			} else {
				return log.traceExit("loginUser(" + username + ", pwd) denied: {}", null);
			}
		} catch (LoginException e) {
			log.error("loginUser({}) causes: {}", username, e.getMessage());
			throw new LoginException(e.getMessage(), e.getType());
		}
	}

	@Override
	public UserDTO loginFromSessionServer() {
		return getUserAlreadyFromSession();
	}

	private UserDTO getUserAlreadyFromSession() {
		UserDTO user = null;
		HttpSession session = this.getThreadLocalRequest().getSession();
		Object userObj = session.getAttribute("USER");
		if (userObj != null && userObj instanceof UserDTO) {
			user = (UserDTO) userObj;
		}
		return user;
	}

	public Boolean logoffUser() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);

		session.invalidate();
		return true;
	}

	public Boolean queryResetPassword(String username) {
		// TODO Auto-generated method stub
		return true;
	}
}
