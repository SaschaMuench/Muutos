package de.sonnmatt.muutos.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.sonnmatt.muutos.HibernateUtil;
import de.sonnmatt.muutos.enums.TenantTypes;
import de.sonnmatt.muutos.utils.StringListConverter;

@Entity
@Table(name = "Muutos_Tenants")
public class TenantJPA {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
	private String	id;
	@Column(name = "Code", updatable = true, nullable = false, unique = true, length = 20)
	private String code;
	@Column(name = "Name", updatable = true, nullable = false, unique = false, length = 100)
	private String name;
	@Column(name = "UrlPart", updatable = true, nullable = false, unique = false, length = 50)
	private String urlPart;
	@Column(name = "Type", updatable = true, nullable = false, unique = false)
	private TenantTypes type;
	@Column(name = "Active", updatable = true, nullable = false, unique = false)
	private Boolean isActive = true;
	@Column(name = "IsTest", updatable = true, nullable = false, unique = false)
	private Boolean isTest = false;
	@Column(name = "Layer", updatable = true, nullable = false, unique = false)
	private int layer = 0;
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="ParentId")
	private TenantJPA parent = null;
	@Column(name = "Structure", updatable = true, nullable = false, unique = false, length = 250)
	@Convert(converter = StringListConverter.class)
	private List<String> structure = null;
	
	@OneToMany(cascade = CascadeType.REFRESH)
	private List<CompanyJPA> companies;

	public TenantJPA() {
	}
	public TenantJPA(String code, String name, String urlPart, Boolean active, Boolean test) {
		super();
		this.code = code;
		this.name = name;
		this.urlPart = urlPart;
		this.isActive = active;
		this.isTest = test;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlPart() {
		return urlPart;
	}
	public void setUrlPart(String urlPart) {
		this.urlPart = urlPart;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsTest() {
		return isTest;
	}
	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}
	public List<CompanyJPA> getCompanies() {
		return companies;
	}
	public void setCompanies(List<CompanyJPA> companies) {
		this.companies = companies;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public TenantJPA getParent() {
		return parent;
	}
	public void setParent(TenantJPA parent) {
		this.parent = parent;
		if (parent == null) {
			this.structure = null;
		} else {
			this.structure = parent.getParentStructure();
		}
	}
	public void setParent(String parentCode) {
		if (parentCode == null || parentCode.isEmpty()) {
			this.parent = null;
			this.structure = null;
		} else {
			EntityManager entityManager = HibernateUtil.getEntityManager();
			List<TenantJPA> tenants = entityManager.createQuery("from TenantJPA where Code = :code order by layer", TenantJPA.class)
													.setParameter("code", parentCode)
													.getResultList();
			setParent(tenants.get(0));
		}
		
	}
	public TenantTypes getType() {
		return type;
	}
	public void setType(TenantTypes type) {
		this.type = type;
	}
	public void setType(String type) {
		this.type = TenantTypes.valueOf(type);
	}
	public String getStructureString() {
		return ((new StringListConverter()).convertToDatabaseColumn(this.structure));
	}
	public List<String> getStructure() {
		List<String> struct = this.structure;
		if (struct == null) {
			struct = new ArrayList<>();
		}
		if (struct.contains(this.id)) {
			struct.remove(this.id);
		}
		return struct;
	}
	public List<String> getParentStructure() {
		List<String> struct = this.structure;
		if (struct == null) {
			struct = new ArrayList<>();
		}
		struct.add(this.id);
		return struct;
	}
	public void setStructure(List<String> structure) {
		if (structure.contains(this.id)) {
			structure.remove(this.id);
		}
		this.structure = structure;
	}
}