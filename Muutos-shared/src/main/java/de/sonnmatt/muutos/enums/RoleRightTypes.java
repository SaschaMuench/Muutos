package de.sonnmatt.muutos.enums;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum RoleRightTypes implements Serializable, IsSerializable {
	Menu1Read, 				// unklare Belege
	Menu1Sub1Read,			// offene Rückfragen
	Menu1Sub2Read,			// beantwortete Rückfragen
	Menu1Sub3Read,			// verarbeitete Rückfragen
	Menu1Sub4Read,			// alle Rückfragen
	Menu1Sub5Read,			// terminierte Rückfragen
	Menu1Modify, 			// unklare Belege
	Menu1Sub1Modify,		// offene Rückfragen
	Menu1Sub2Modify,		// beantwortete Rückfragen
	Menu1Sub3Modify,		// verarbeitete Rückfragen
	Menu1Sub4Modify,		// alle Rückfragen
	Menu1Sub5Modify,		// terminierte Rückfragen
	Menu2, 					// view menu 2
	Menu2Sub1,
	Menu2Sub2,
	Menu2Sub3,
	Menu2Sub4,
	Menu2Sub5,
	Menu3, 					// view menu 3
	Menu3Sub1,
	Menu3Sub2,
	Menu3Sub3,
	Menu3Sub4,
	Menu3Sub5,
	UserMngmnt, 			// view users and user management
	UserMngmntCreateUser, 	// create new users
	UserMngmntModifyUser, 	// modify existing users
	UserMngmntResetPassword;// reset passwords for users

	@Override
	public String toString() {
		return super.toString();
	}

}
