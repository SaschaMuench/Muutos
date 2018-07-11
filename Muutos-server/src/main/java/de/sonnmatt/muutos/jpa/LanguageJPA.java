package de.sonnmatt.muutos.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({@NamedQuery(name = LanguageJPA.GetLanAll, query = LanguageJPA.GetLanAll_Query),
	@NamedQuery(name = LanguageJPA.GetLanByCode, query = LanguageJPA.GetLanByCode_Query) })

@Entity
@Table(name = "Muutos_Languages")
public class LanguageJPA {
	
	public static final String GetLanAll = "LanguageJPA_GetAll";
	public static final String GetLanByCode = "LanguageJPA_GetByCode";

	public static final String ParamLanCode = "code";

	public static final String GetLanAll_Query = "from LanguageJPA";
	public static final String GetLanByCode_Query = "from LanguageJPA where Code = :" + ParamLanCode;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
	private String	id;
	@Column(name = "Code", updatable = false, nullable = false, unique = true)
	private String	code;
	@Column(name = "Description", updatable = false, nullable = false, unique = false)
	private String	description;
	
	public LanguageJPA() {
	}
	
	public LanguageJPA(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
