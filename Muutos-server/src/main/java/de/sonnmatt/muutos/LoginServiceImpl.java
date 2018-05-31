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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.enums.TranslationSections;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.exceptions.LoginException;
import de.sonnmatt.muutos.exceptions.LoginException.LoginExceptionType;
import de.sonnmatt.muutos.jpa.LanguageJPA;
import de.sonnmatt.muutos.jpa.TranslationJPA;
import de.sonnmatt.muutos.jpa.UserJPA;
import de.sonnmatt.muutos.rpc.LoginService;

/**
 * @author MuenSasc
 *
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService, IsSerializable {

	public LoginServiceImpl() {
		super();
	}

	public LoginServiceImpl(Object delegate) {
		super(delegate);
	}

	private static final long serialVersionUID = -1602894349360490809L;
	
	private static Logger log = LogManager.getLogger(LoginServiceImpl.class);
	private static EntityManager entityManager = HibernateUtil.getEntityManager();

	@Override
	public TranslationsDTO getText(String language, TranslationSections action) throws LoginException {
		log.traceEntry("getText({}, {})", language, action.toString());
		
		if (language.length() > 2) language = language.substring(0, language.indexOf("-")).toUpperCase();
		language = language.toUpperCase();
		List<LanguageJPA> languages = entityManager.createQuery("from LanguageJPA where Code = :lan", LanguageJPA.class)
														.setParameter("lan", language)
														.getResultList();
		if (languages.size() == 0) {
			languages = entityManager.createQuery("from LanguageJPA", LanguageJPA.class).getResultList();
			if (languages.size() == 0) {
				InitializeSetup init = new InitializeSetup();
				init.setupSystem();
			} else {
				log.error("{} getText({}, {}): unknown language", this.getClass().getSimpleName(), language, action.toString());
				throw(new LoginException("Unknown language: " + language, LoginExceptionType.generalError));
			}
		}
		
		List<TranslationJPA> translations;
		translations = entityManager.createQuery("from TranslationJPA where lanCode = :lan and Code like :action", TranslationJPA.class)
										.setParameter("lan", language.toUpperCase())
										.setParameter("action", action.toString() + ".%")
										.getResultList();
		
		log.trace("getText(): translations found: " + translations.size());
		TranslationsDTO transDTO = new TranslationsDTO();
		translations.forEach(t -> transDTO.set(t.getCode(), t.getTranslation()));
		log.trace("getText(): translations copied");
		
		return transDTO;
	}

	@Override
	public Boolean queryResetPassword(String username) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public UserDTO loginUser(String username, String password) throws LoginException {
		// TODO Auto-generated method stub
		log.traceEntry("{} loginUser({}, pwd)", this.getClass().getSimpleName(), username);

		//entityManager.getTransaction().begin();

		log.trace("{} loginUser entityManager started", this.getClass().getSimpleName());
		List<UserJPA> user = entityManager.createQuery("from UserJPA where LoginName = :username", UserJPA.class)
											.setParameter("username", username)
											.getResultList();
		log.trace("{} loginUser List<UserJPA> generated. Size: {}", this.getClass().getSimpleName(), user.size());

		if (user.size() == 0) {
			// Check first user --> add as Superuser
			List<UserJPA> allUsers = entityManager.createQuery("from UserJPA", UserJPA.class).getResultList();
			log.trace("{} loginUser allUser generated. Size: {}", this.getClass().getSimpleName(), allUsers.size());
			if (allUsers.size() == 0) {
				saveSuperUser(username, password);
				log.trace("{} loginUser get users after generation", this.getClass().getSimpleName());
				user = entityManager.createQuery("from UserJPA where LoginName = :username", UserJPA.class)
										.setParameter("username", username)
										.getResultList();
				log.trace("{} loginUser List<UserJPA> generated. Size: {}", this.getClass().getSimpleName(), user.size());
			} else
				throw new LoginException(username, LoginExceptionType.userUnknown);
		}
		if (user.get(0).getActive().equals(false))
			throw new LoginException(username, LoginExceptionType.loginLocked);
		if (!user.get(0).checkPassword(password))
			throw new LoginException("", LoginExceptionType.passwordWrong);

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        
		UserDTO userDto = user.get(0).generateDTO();
		userDto.set(UserFields.SessionID, session.getId());
		session.setAttribute("user", userDto);
        
		return userDto;
	}

	public Boolean logoffUser() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        
        session.invalidate();
        return true;
	}
	
	private void saveSuperUser(String username, String password) {
		log.traceEntry("{} saveSuperUser({}, pwd)", this.getClass().getSimpleName(), username);
		UserJPA user = new UserJPA();
		//user.setUid("00000000-0000-0000-0000-000000000000")
		user.setLoginName(username);
		user.setPassword(password);
		user.setPwdMustChange(false);
		user.setActive(true);
		user.setLanCode("EN");
		//user.setCompany("Tessi");
		//user.setFirstName(" ");
		//user.setLastName(" ");
		user.seteMail("muenchs@gmx.net");
		
		log.trace("{} saveSuperUser() hashedPwd: {}", this.getClass().getSimpleName(), user.getPassword());
		
		EntityManager entityManager = HibernateUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.flush();
		entityManager.getTransaction().commit();
	}
}
