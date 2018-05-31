package de.sonnmatt.muutos.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.sonnmatt.muutos.Encryption;
import de.sonnmatt.muutos.HibernateUtil;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.enums.TranslationSections;
import de.sonnmatt.muutos.enums.UserFields;

@Entity
@Table(name = "Muutos_Users")
public class UserJPA implements Serializable {

	private static final long serialVersionUID = -575808733752686494L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
	private String	uid;
	@Column(name = "LoginName", length = 50, unique = false, nullable = false)
	private String	loginName		= "";
	@Column(name = "Password", length = 100, unique = false, nullable = false)
	private String	password		= "";
	@Column(name = "Active", unique = false, nullable = false)
	private Boolean	active			= false;
	@Column(name = "PwdMustChange", unique = false, nullable = false)
	private Boolean	pwdMustChange	= false;
	@Column(name = "LanCode", length = 2, unique = false, nullable = false)
	private String	lanCode			= "";
	@Column(name = "Company", length = 50, unique = false, nullable = false)
	private String	company			= "";
	@Column(name = "FirstName", length = 50, unique = false, nullable = false)
	private String	firstName	= "";
	@Column(name = "LastName", length = 100, unique = false, nullable = false)
	private String	lastName	= "";
	@Column(name = "Street", length = 100, unique = false, nullable = true)
	private String	street;
	@Column(name = "ZIP", length = 10, unique = false, nullable = true)
	private String	zip;
	@Column(name = "City", length = 50, unique = false, nullable = true)
	private String	city;
	@Column(name = "Country", length = 2, unique = false, nullable = true)
	private String	country;
	@Column(name = "eMail", length = 100, unique = false, nullable = false)
	private String	eMail		= "";
	@Column(name = "Phone", length = 40, unique = false, nullable = true)
	private String	phone;
	@Column(name = "Mobile", length = 40, unique = false, nullable = true)
	private String	mobile;
	
	@OneToOne()
	@PrimaryKeyJoinColumn
	private TenantJPA homeTenenat;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="Muutos_UserGroups",
				joinColumns={@JoinColumn(name="UserId", referencedColumnName="id")},
				inverseJoinColumns={@JoinColumn(name="GroupId", referencedColumnName="id")})
    private List<GroupJPA> userGroups;
	
	public UserJPA() {
	}
	
	public UserJPA(UserDTO user) {
		//this.uid  = UUID.fromString(user.get(UserFields.UID));
		this.uid  = user.get(UserFields.UID);
		this.loginName = user.get(UserFields.Login);
		this.password = user.get(UserFields.Password);
		this.pwdMustChange = Boolean.valueOf(user.get(UserFields.PasswortMustChange));
		this.active = Boolean.valueOf(user.get(UserFields.Active));
		this.lanCode = user.get(UserFields.Language);
		this.firstName = user.get(UserFields.FirstName);
		this.lastName = user.get(UserFields.LastName);
		this.street = user.get(UserFields.Street);
		this.zip = user.get(UserFields.ZIP);
		this.city = user.get(UserFields.City);
		this.country = user.get(UserFields.Country);
		this.eMail = user.get(UserFields.eMail);
		this.company = user.get(UserFields.Company);
		this.phone  = user.get(UserFields.Phone);
		this.mobile  = user.get(UserFields.Mobile);
//		this.homeTenenat = user.get(UserFields.TenantID);
		EntityManager entityManager = HibernateUtil.getEntityManager();
		List<TenantJPA> tenants = entityManager.createQuery("from TenantJPA where id = :id", TenantJPA.class)
															.setParameter("id", user.get(UserFields.TenantID))
															.getResultList();
		
	}

	public UserDTO generateDTO() {
		UserDTO user = new UserDTO();
		user.set(UserFields.UID, this.uid.toString());
		user.set(UserFields.Login, this.loginName);
		user.set(UserFields.Login, this.loginName);
		user.set(UserFields.Active, this.active);
		user.set(UserFields.PasswortMustChange, this.pwdMustChange);
		user.set(UserFields.Language, this.lanCode);
		user.set(UserFields.FirstName, this.firstName);
		user.set(UserFields.LastName, this.lastName);
		user.set(UserFields.Street, this.street);
		user.set(UserFields.ZIP, this.zip);
		user.set(UserFields.City, this.city);
		user.set(UserFields.Country, this.country);
		user.set(UserFields.eMail, this.eMail);
		user.set(UserFields.Company, this.company);
		user.set(UserFields.Phone, this.phone);
		user.set(UserFields.Mobile, this.mobile);
//		user.set(UserFields.TenantID, this.homeTenenat.getId());
//		user.set(UserFields.TenantCode, this.homeTenenat.getCode());
		
		return user;
	}

	// {{ Getter and Setter
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Encryption.hashPassword(password);
	}
	
	public Boolean checkPassword(String checkpassword) {
		return Encryption.checkPassword(checkpassword, this.password);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getPwdMustChange() {
		return pwdMustChange;
	}

	public void setPwdMustChange(Boolean pwdMustChange) {
		this.pwdMustChange = pwdMustChange;
	}

	public String getLanCode() {
		return lanCode;
	}

	public void setLanCode(String lanCode) {
		Locale testLanCode = new Locale(lanCode);
		this.lanCode = testLanCode.getLanguage();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		Locale testCountry = new Locale("", country);
		this.country = testCountry.getCountry();
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setUid1(String uid) {
		this.uid = uid;
	}
	public UserJPA setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public TenantJPA getHomeTenenat() {
		return homeTenenat;
	}

	public void setHomeTenenat(TenantJPA homeTenenat) {
		this.homeTenenat = homeTenenat;
	}
}
