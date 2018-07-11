/**
 * 
 */
package de.sonnmatt.muutos.rpc.logging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.exceptions.LoginException;

/**
 * @author MuenSasc
 * functions to process login for the application
 */
@RemoteServiceRelativePath("clientlogging")
public interface ClientLogging extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ClientLoggingAsync instance;

		public static ClientLoggingAsync getInstance() {
			if (instance == null) instance = GWT.create(ClientLogging.class);
			return instance;
		}
	}
	
	public TextResourcesDTO getText(String language);

	public Boolean queryResetPassword(String username);
	
	public UserDTO loginUser(String username, String password) throws LoginException;
	
}
