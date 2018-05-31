package de.sonnmatt.muutos.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;

import de.sonnmatt.muutos.parser.Parser;

@Entity
@Table(name = "Muutos_Translations")
public class TranslationJPA implements Serializable {

	private static final long serialVersionUID = -5922854973895111013L;
	static Logger log = LogManager.getLogger(TranslationJPA.class);
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, updatable = false, nullable = false, length = 36)
	private String	id;
	@Column(name = "Code", unique = false, nullable = false)
	private String	code				= "";
	@Column(name = "LanCode", unique = false, nullable = false, length = 2)
	private String	language			= "";
	@Column(name = "Translation", unique = false, nullable = true)
	private String	translation;
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="TenantId")
	private TenantJPA tenant;
	
	public TranslationJPA() {
	}

	/**
	 * @param tenant
	 * @param code
	 * @param language
	 * @param translation
	 */
	public TranslationJPA(TenantJPA tenant, String code, String language, String translation) {
		super();
		this.tenant = tenant;
		this.code = code;
		this.language = language;
		this.translation = translation;
	}

	public String getId() {
		return id;
	}

	public TranslationJPA setId(String id) {
		this.id = id;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTranslationUnformated() {
		return ((translation == null) ? "" : translation);
	}

	public String getTranslation() {
		log.traceEntry("getTranslation: " + this.code);
		String trans = (translation == null) ? "" : translation;
		if (trans.contains("{") && trans.contains("}")) {
			log.trace("translation contains '{' and '}'");
			trans = new Parser(trans).eval();
		}
		return log.traceExit("getTranslation: " + this.code + ": {}", trans);
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public TenantJPA getTenant() {
		return tenant;
	}

	public void setTenant(TenantJPA tenant) {
		this.tenant = tenant;
	}
}