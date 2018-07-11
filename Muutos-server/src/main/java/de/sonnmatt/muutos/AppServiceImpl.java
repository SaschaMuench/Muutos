/**
 * 
 */
package de.sonnmatt.muutos;

import static de.sonnmatt.muutos.jpa.TextJPA.*;
import static de.sonnmatt.muutos.jpa.TextSectionsExt.*;
import static de.sonnmatt.muutos.SessionAttributes.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.DataDTO.dataTypes;
import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.exceptions.GeneralException;
import de.sonnmatt.muutos.exceptions.GeneralException.GeneralExceptionType;
import de.sonnmatt.muutos.exceptions.LoginException;
import de.sonnmatt.muutos.exceptions.LoginException.LoginExceptionType;
import de.sonnmatt.muutos.jpa.TextJPA;
import de.sonnmatt.muutos.rpc.AppService;

/**
 * @author MuenSasc
 *
 */
public class AppServiceImpl extends RemoteServiceServlet implements AppService, IsSerializable {

	private static final long serialVersionUID = 4800408736235709473L;

	private static Logger log = LogManager.getLogger(AppServiceImpl.class);
	private static EntityManager entityManager = HibernateUtil.getEntityManager();

	public AppServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppServiceImpl(Object delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	/* 
	 * ToDo: rename to ????
	 */
	@Override
	public DataDTO getData(String sessionID) throws LoginException, GeneralException {
		log.traceEntry("DataDTO getData(" + sessionID + ")");

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);

		String serverSessionID = session.getId();
		if (!serverSessionID.equals(sessionID)) {
			log.error("Session ID mismatch!! Server({}) vs payload({})" + serverSessionID, sessionID);
			throw (new LoginException("Hacking detected", LoginExceptionType.generalError));
		}
		String serverTenantID = (String) session.getAttribute(SESSATTR_TENANT_ID);
		UserDTO userDTO = (UserDTO) session.getAttribute(SESSATTR_USER);
		String lanCode = userDTO.get(UserFields.Language);

		TextService textServ = new TextService();
		TextResourcesDTO transDTO = null;
		try {
			transDTO = textServ.setLanguage(lanCode).setTenant(serverTenantID).getTexts(APP_BASE);
		} catch (GeneralException e) {
			log.error("GetTexts({}, {}, {}) failed", lanCode, serverTenantID, APP_BASE);
			throw (new GeneralException("Tenant unknown", GeneralExceptionType.unknownTenant));
		}

		ViewDTO viewDTO = new ViewDTO();
		viewDTO.set(APP_BASE + ".view.key", true);

		DataDTO data = new DataDTO();
		data.set(dataTypes.Translation, transDTO);
		data.set(dataTypes.View, viewDTO);

		return data;
	}

	@Override
	public TextResourcesDTO getText(String sessionID) throws LoginException {
		log.traceEntry("TranslationsDTO getText(" + sessionID + ")");

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);

		String serverSessionID = session.getId();
		if (!serverSessionID.equals(sessionID)) {
			log.error("Session ID mismatch!! Server({}) vs payload({})" + serverSessionID, sessionID);
			throw (new LoginException("Hacking detected", LoginExceptionType.generalError));
		}

		UserDTO userDTO = (UserDTO) session.getAttribute(SESSATTR_USER);
		List<TextJPA> translations = entityManager	.createNamedQuery(GetTextByTenant_Lan_CodeLike, TextJPA.class)
													.setParameter(ParamTextLan, userDTO.get(UserFields.Language))
													.setParameter(ParamTextCode, APP_BASE + ".%")
													.getResultList();
		TextResourcesDTO transDTO = new TextResourcesDTO();
		translations.forEach(t -> transDTO.put(t.getCode(), t.getTranslation()));

		return transDTO;
	}

	@Override
	public ViewDTO getView(String sessionID) {
		log.traceEntry("DataDTO getView(" + sessionID + ")");

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);

		String serverSessionID = session.getId();
		log.trace("Server Session ID: " + serverSessionID);
		UserDTO userDTO = (UserDTO) session.getAttribute("user");
		log.trace("Server userDTO SID: " + userDTO.getSessionId());

		ViewDTO viewDTO = new ViewDTO();
		viewDTO.set(APP_BASE + ".view.key", true);

		return viewDTO;
	}

}
