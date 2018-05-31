package de.sonnmatt.muutos.enums;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum UserFields implements Serializable, IsSerializable {
	UID, FirstName, LastName, Login, Password, PasswortMustChange, Active, 
	eMail, Language, Street, ZIP, City, Country, Phone, Mobile, SessionID, LoggedIn, Company, TenantID, TenantCode;

	@Override
	public String toString() {
		return super.toString();
	}
}
