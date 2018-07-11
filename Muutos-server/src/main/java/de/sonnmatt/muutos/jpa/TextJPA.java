package de.sonnmatt.muutos.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import de.sonnmatt.muutos.parser.Parser;

@NamedQueries({
		@NamedQuery(name = TextJPA.GetTextByTenant_Lan_CodeLike, query = TextJPA.GetTextByTenant_Lan_CodeLikeQuery) })

@Entity
@Table(name = "Muutos_Texts")
public class TextJPA implements Serializable {

	private static final long serialVersionUID = -5922854973895111013L;
	static Logger log = LogManager.getLogger(TextJPA.class);

	public static final String GetTextByTenant_Lan_CodeLike = "TextJPA_GetByTenant_Lan_CodeLike";

	public static final String ParamTextCode = "code";
	public static final String ParamTextLan = "lan";
	public static final String ParamTextTenantId = "tenantid";

	protected static final String GetTextByTenant_Lan_CodeLikeQuery = "FROM TextJPA WHERE TenantId = :" + ParamTextTenantId
			+ " AND LanCode = :" + ParamTextLan + " AND Code LIKE :" + ParamTextCode;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", unique = true, updatable = false, nullable = false, length = 36)
	private String id;
	@Column(name = "Code", unique = false, nullable = false)
	private String code = "";
	@Column(name = "LanCode", unique = false, nullable = false, length = 2)
	private String language = "";
	@Column(name = "Translation", unique = false, nullable = true)
	private String translation;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TenantId")
	private TenantJPA tenant;

	public TextJPA() {
	}

	/**
	 * @param tenant
	 * @param code
	 * @param language
	 * @param translation
	 */
	public TextJPA(TenantJPA tenant, String code, String language, String translation) {
		super();
		this.tenant = tenant;
		this.code = code;
		this.language = language;
		this.translation = translation;
	}

	public String getId() {
		return id;
	}

	public TextJPA setId(String id) {
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