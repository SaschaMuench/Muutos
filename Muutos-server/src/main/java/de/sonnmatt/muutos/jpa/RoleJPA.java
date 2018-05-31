package de.sonnmatt.muutos.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Muutos_Roles")
public class RoleJPA {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String	id;
	@Column(name = "RoleID", updatable = false, nullable = false, unique = true)
	private String	roleId;
	@Column(name = "Description", updatable = false, nullable = false, unique = false)
	private String	description;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoleRightJPA> rights; // = new ArrayList<>();
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<RoleRightJPA> getRights() {
		return rights;
	}
	public void setRights(List<RoleRightJPA> rights) {
		this.rights = rights;
	}
}
