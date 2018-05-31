package de.sonnmatt.muutos.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Muutos_Languages")
public class LanguageJPA {
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
