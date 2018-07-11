package de.sonnmatt.muutos.jpa;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.ParamDef;

import de.sonnmatt.muutos.HibernateUtil;
import de.sonnmatt.muutos.enums.TenantTypes;
import de.sonnmatt.muutos.utils.StringListConverter;

@NamedQueries({ @NamedQuery(name = TenantJPA.GetTenantAll, query = TenantJPA.GetTenantAllQuery),
		@NamedQuery(name = TenantJPA.GetTenantTop, query = TenantJPA.GetTenantTopQuery),
		//@NamedQuery(name = TenantJPA.GetTenantByID, query = TenantJPA.GetTenantByIDQuery),
		@NamedQuery(name = TenantJPA.GetTenantByUrl, query = TenantJPA.GetTenantByUrlQuery),
		@NamedQuery(name = TenantJPA.GetTenantByCode_OrderLevel, query = TenantJPA.GetTenantByCode_OrderLevelQuery) })

@FilterDef(name = TenantJPA.FilterTenantMultiTenant, parameters = {
		@ParamDef(name = TenantJPA.FilterTenantParamTenantId, type = "string") })
@Filters({ @Filter(name = TenantJPA.FilterTenantMultiTenant, condition = "structure like '%' + :tenantdId + '%'") })

@Entity
@Table(name = "Muutos_Tenants")
@EntityListeners({ HierarchyListener.class })
public class TenantJPA implements IHierarchyElement {

	public static final String GetTenantAll = "TenantJPA_GetAll";
	public static final String GetTenantTop = "TenantJPA_GetTop";
	//public static final String GetTenantByID = "TenantJPA_GetByID";
	public static final String GetTenantByUrl = "TenantJPA_GetByUrl";
	public static final String GetTenantByCode_OrderLevel = "TenantJPA_GetByCode_OrderLevel";

	public static final String FilterTenantMultiTenant = "TenantJPA_FilterMultiTenant";
	public static final String FilterTenantParamTenantId = "tenantId";

	//public static final String ParamTenantId = "id";
	public static final String ParamTenantCode = "code";
	public static final String ParamTenantUrl = "url";

	protected static final String GetTenantAllQuery = "FROM TenantJPA";
	protected static final String GetTenantTopQuery = "FROM TenantJPA WHERE Level = 0";
	//protected static final String GetTenantByIDQuery = "FROM TenantJPA WHERE Id = :" + ParamTenantId;
	protected static final String GetTenantByUrlQuery = "FROM TenantJPA WHERE UrlPart = :" + ParamTenantUrl;
	protected static final String GetTenantByCode_OrderLevelQuery = "FROM TenantJPA WHERE Code = :" + ParamTenantCode
			+ " order by Level";

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
	private String id;
	@Column(name = "Code", updatable = true, nullable = false, unique = true, length = 20)
	private String code;
	@Column(name = "Name", updatable = true, nullable = false, unique = false, length = 100)
	private String name;
	@Column(name = "UrlPart", updatable = true, nullable = false, unique = false, length = 50)
	private String urlPart;
	@Column(name = "Type", updatable = true, nullable = false, unique = false)
	private TenantTypes type;
	@Column(name = "isActive", updatable = true, nullable = false, unique = false)
	private Boolean isActive = true;
	@Column(name = "isTest", updatable = true, nullable = false, unique = false)
	private Boolean isTest = false;
	@Column(name = "Level", updatable = true, nullable = false, unique = false)
	private int level = 0;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ParentId")
	private TenantJPA parent = null;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TopTenant")
	private TenantJPA topTenant = null;
	@Column(name = "Structure", updatable = true, nullable = false, unique = false, length = 250)
	@Convert(converter = StringListConverter.class)
	private List<String> structure = null;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public TenantJPA getParent() {
		return parent;
	}

	public void setParent(TenantJPA parent) {
		this.parent = parent;
	}

	public void setParent(String parentCode) {
		if (parentCode == null || parentCode.isEmpty()) {
			this.parent = null;
			this.structure = null;
		} else {
			EntityManager entityManager = HibernateUtil.getEntityManager();
			List<TenantJPA> tenants = entityManager.createQuery("from TenantJPA where Code = :code order by layer",
					TenantJPA.class).setParameter("code", parentCode).getResultList();
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
		return this.structure;
	}

	public List<String> getParentStructure() {
		List<String> struct = this.structure;
		struct.remove(this.id);
		return struct;
	}

	public void setStructure(List<String> structure) {
		this.structure = structure;
	}

	public IHierarchyElement getTop() {
		return topTenant;
	}

	public void setTop(IHierarchyElement top) {
		this.topTenant = (TenantJPA) top;
	}

	public Boolean isTop() {
		return (type == TenantTypes.Tenant);
	}
}