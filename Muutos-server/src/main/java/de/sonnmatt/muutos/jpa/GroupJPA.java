package de.sonnmatt.muutos.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Muutos_Groups")
public class GroupJPA {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String	id;
	@Column(name = "GroupID", updatable = true, nullable = false, unique = true)
	private String	groupID;
	@Column(name = "Description", updatable = true, nullable = false, unique = false)
	private String	description;
	@OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="Muutos_UserGroups",
				joinColumns={@JoinColumn(name="UserId", referencedColumnName="id")},
				inverseJoinColumns={@JoinColumn(name="GroupId", referencedColumnName="id")})
	private List<UserJPA> userList;
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="ParentId")
	private TenantJPA parent;
}
