package de.sonnmatt.muutos.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Muutos_Companies")
public class CompanyJPA {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String	id;
	@Column(name = "Code", updatable = true, nullable = false, unique = false)
	private String code;
	@Column(name = "Name", updatable = true, nullable = false, unique = false)
	private String name;
	@Column(name = "AddressLine1", updatable = true, nullable = false, unique = false)
	private String addressLine1;
	@Column(name = "AddressLine2", updatable = true, nullable = false, unique = false)
	private String addressLine2;
	@Column(name = "AddressLine3", updatable = true, nullable = false, unique = false)
	private String addressLine3;
	@Column(name = "Active", updatable = true, nullable = false, unique = false)
	private Boolean active;
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="ParentId")
	private TenantJPA parent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public TenantJPA getParent() {
		return parent;
	}
	public void setParent(TenantJPA parent) {
		this.parent = parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
}
