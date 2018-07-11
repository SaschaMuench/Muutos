package de.sonnmatt.muutos.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = SystemParamameterJPA.GetAll, query = "from SystemParamameterJPA") })

@Entity
@Table(name = "Muutos_SystemParamameters")
public class SystemParamameterJPA extends ParameterJPA {

	public static final String GetAll = "GetAll";
		
	public enum SystemParameterCodes {
		Initialized
	}
	
	public void setCode(SystemParameterCodes code) {
		super.setCode(code.toString());
	}
}
