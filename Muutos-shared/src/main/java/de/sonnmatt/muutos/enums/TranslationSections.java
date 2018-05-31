package de.sonnmatt.muutos.enums;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TranslationSections implements Serializable, IsSerializable {
	/*UserManagement,*/
	Application,
	AppBase,
	Login,
	Logoff,
	Timeout;
	
	@Override
	public String toString() {
		return super.toString();
	}
}
