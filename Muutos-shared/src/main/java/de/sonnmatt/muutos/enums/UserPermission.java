package de.sonnmatt.muutos.enums;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum UserPermission implements Serializable, IsSerializable {
	Menu1, Menu2, Menu3, UserMngmnt, UserMngmntCreateUser, UserMngmntModifyUser, UserMngmntResetPassword; 

	@Override
	public String toString() {
		return super.toString();
	}

}
