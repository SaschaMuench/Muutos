package de.sonnmatt.muutos.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.sonnmatt.muutos.enums.RoleRightTypes;

@Entity
@Table(name = "Muutos_RoleRights")
public class RoleRightJPA {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String	id;
	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "RoleID", updatable = false, nullable = false, unique = false)
	private String	roleId;
	@Column(name = "RightType", updatable = false, nullable = false, unique = false, columnDefinition = "VARCHAR2(30)")
	private RoleRightTypes	rightType;
	@Column(name = "RightType", updatable = true, nullable = false, unique = false)
	private boolean	rightValue;
	
	public RoleRightTypes getRightType() {
		return rightType;
	}
	public void setRightType(RoleRightTypes rightType) {
		this.rightType = rightType;
	}
	public boolean getRightValue() {
		return rightValue;
	}
	public void setRightValue(boolean rightValue) {
		this.rightValue = rightValue;
	}
}
