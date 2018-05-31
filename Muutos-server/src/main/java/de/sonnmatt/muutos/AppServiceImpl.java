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

import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.DataDTO.dataTypes;
import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;
import de.sonnmatt.muutos.enums.TranslationSections;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.jpa.TranslationJPA;
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

	@Override
	public DataDTO getData(String sessionID) {
		log.traceEntry("DataDTO getData(" + sessionID + ")");
		
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        
        String serverSessionID = session.getId();
        log.trace("Server Session ID: " + serverSessionID);
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        log.trace("Server userDTO SID: " + userDTO.getSessionId());
        
		List<TranslationJPA> translations = entityManager.createQuery("from TranslationJPA where lanCode = :lan and Code like :action", TranslationJPA.class)
															.setParameter("lan", userDTO.get(UserFields.Language))
															.setParameter("action", TranslationSections.AppBase.toString() + ".%")
															.getResultList();

		TranslationsDTO transDTO = new TranslationsDTO();
		translations.forEach(t -> transDTO.set(t.getCode(), t.getTranslation()));

		ViewDTO viewDTO = new ViewDTO();
        viewDTO.set(TranslationSections.AppBase + ".view.key", true);
 
        DataDTO data = new DataDTO();
		data.set(dataTypes.Translation, transDTO);
        data.set(dataTypes.View, viewDTO);
        
		return data;
	}

	@Override
	public TranslationsDTO getText(String sessionID) {
		log.traceEntry("TranslationsDTO getText(" + sessionID + ")");
		
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        
        String serverSessionID = session.getId();
        log.trace("Server Session ID: " + serverSessionID);
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        log.trace("Server userDTO SID: " + userDTO.getSessionId());
        
		List<TranslationJPA> translations = entityManager.createQuery("from TranslationJPA where lanCode = :lan and Code like :action", TranslationJPA.class)
				.setParameter("lan", userDTO.get(UserFields.Language))
				.setParameter("action", TranslationSections.AppBase.toString() + ".%")
				.getResultList();

		TranslationsDTO transDTO = new TranslationsDTO();
		translations.forEach(t -> transDTO.set(t.getCode(), t.getTranslation()));
        
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
        viewDTO.set(TranslationSections.AppBase + ".view.key", true);
        
        return viewDTO;
	}

}
