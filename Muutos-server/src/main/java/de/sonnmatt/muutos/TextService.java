package de.sonnmatt.muutos;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.exceptions.GeneralException;
import de.sonnmatt.muutos.exceptions.GeneralException.GeneralExceptionType;
import de.sonnmatt.muutos.jpa.LanguageJPA;
import de.sonnmatt.muutos.jpa.TenantJPA;
import de.sonnmatt.muutos.jpa.TextJPA;
import static de.sonnmatt.muutos.jpa.LanguageJPA.*;
import static de.sonnmatt.muutos.jpa.TenantJPA.*;
import static de.sonnmatt.muutos.jpa.TextJPA.*;

public class TextService {
	private static Logger log = LogManager.getLogger(TextService.class);
	private static EntityManager entityManager = HibernateUtil.getEntityManager();

	private static String defaultLanguage = "EN";
	private String language = null;
	private TenantJPA tenant = null;

	/**
	 * Returns the texts for a given section. Takes care of the tenant in the tenant
	 * structure. If there is no text for the tenant, the text for the parent will
	 * be returned. </BR>
	 * In case that there are no text for the the requested language, the texts for
	 * language 'EN' will be returned </BR>
	 * Please make sure to set language and tenant before call <I>getTexts</I>.
	 * 
	 * @param section
	 *            beginning of the code of the translation (section where the code
	 *            belongs to)
	 * @return HashMap with code in upper case and text
	 * @throws GeneralException
	 *             in case that language or tenant are unknown
	 */
	public TextResourcesDTO getTexts(String section) throws GeneralException {
		log.traceEntry("getTexts({})", section);

		if (language == null) {
			log.error("getTexts(): no language");
			throw (new GeneralException("No language", GeneralExceptionType.unknownLanguage));
		}
		if (tenant == null) {
			log.error("getTexts(): no tenant");
			throw (new GeneralException("No tenant", GeneralExceptionType.unknownTenant));
		}

		TextResourcesDTO textMap = new TextResourcesDTO();
		textMap.putAll(readTexts(section, defaultLanguage));
		if (!language.equals(defaultLanguage)) {
			textMap.putAll(readTexts(section, language));
		}
		return textMap;
	}

	private TextResourcesDTO readTexts(String action, String language) {
		log.traceEntry("readTexts({}, {})", action, language);
		TextResourcesDTO textMap = new TextResourcesDTO();
		List<TextJPA> translations = null;
		for (String structID : tenant.getStructure()) {
			log.trace("structID: {}", structID);
			translations = entityManager.createNamedQuery(GetTextByTenant_Lan_CodeLike, TextJPA.class)
										.setParameter(ParamTextTenantId, structID)
										.setParameter(ParamTextLan, language)
										.setParameter(ParamTextCode, action + "%")
										.getResultList();
			log.trace("readTexts translation.size(): {}", translations.size());
			translations.forEach(t -> textMap.put(t.getCode(), t.getTranslation()));
		}
		return textMap;
	}

	public TextService setLanguage(String language) throws GeneralException {
		log.traceEntry("setLanguage({})", language);

		if (language.length() > 2)
			language = language.substring(0, language.indexOf("-")).toUpperCase();
		List<LanguageJPA> languages = entityManager	.createNamedQuery(GetLanByCode, LanguageJPA.class)
													.setParameter(ParamLanCode, language)
													.getResultList();
		if (languages.size() == 0) {
			log.error("setLanguage({}): unknown language", language);
			throw (new GeneralException("Unknown language: " + language, GeneralExceptionType.unknownLanguage));
		}

		this.language = language.toUpperCase();
		return this;
	}

	public TextService setTenant(String tenantID) throws GeneralException {
		log.traceEntry("setTenant({})", tenantID);

		TenantJPA tenant = entityManager.find(TenantJPA.class, tenantID);		
		if (tenant == null) {
			log.error("setTenant({}): unknown tenant", tenantID);
			throw (new GeneralException("Unknown tenant: " + tenantID, GeneralExceptionType.unknownTenant));
		}

		this.tenant = tenant;
		return this;
	}

	public TextService setTenantByCode(String code) throws GeneralException {
		log.traceEntry("setTenantByCode({})", code);

		TenantJPA tenant = entityManager.createNamedQuery(GetTenantByCode_OrderLevel, TenantJPA.class)
										.setParameter(ParamTenantCode, code)
										.getSingleResult();
		if (tenant == null) {
			log.error("setTenantByCode({}): unknown tenant", code);
			throw (new GeneralException("Unknown tenant: " + code, GeneralExceptionType.unknownTenant));
		}

		this.tenant = tenant;
		return this;
	}

	public TextService setTenantByUrl(String tenantUrl) throws GeneralException {
		log.traceEntry("setTenantByUrl({})", tenantUrl);

		TenantJPA tenant = entityManager.createNamedQuery(GetTenantByUrl, TenantJPA.class)
										.setParameter(ParamTenantUrl, tenantUrl)
										.getSingleResult();
		if (tenant == null) {
			log.error("setTenant({}): unknown tenant", tenantUrl);
			throw (new GeneralException("Unknown tenant: " + tenantUrl, GeneralExceptionType.unknownTenant));
		}

		this.tenant = tenant;
		return this;
	}
}