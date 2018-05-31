package de.sonnmatt.muutos;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

import de.sonnmatt.muutos.DTOs.UserDTO;

public class AppLocalData {

	private static String sessionID;
	private static UserDTO userDTO;

	public static void storeSessionID(String sessionID) {
        //set session cookie for 1 day expire.
		final long DURATION = 1000 * 60 * 60 * 24 * 1;
		Date expires = new Date(System.currentTimeMillis() + DURATION);
		Cookies.setCookie("muutos_sid", sessionID, expires, null, "/", false);
		AppLocalData.sessionID = sessionID;
	}
	
	public static String readCookieSessionID() {
		return Cookies.getCookie("muutos_sid");
	}
	
	public static String getSessionID() {
		return sessionID;
	}

	public static UserDTO getUserDTO() {
		return userDTO;
	}

	public static void setUserDTO(UserDTO userDTO) {
		AppLocalData.userDTO = userDTO;
	}
	
}
