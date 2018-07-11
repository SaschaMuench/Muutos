/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.exceptions.LoginException;

/**
 * @author MuenSasc functions to process login for the application
 */
@RemoteServiceRelativePath("loginservice")
public interface LoginService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static LoginServiceAsync instance;

		public static LoginServiceAsync getInstance() {
			if (instance == null)
				instance = GWT.create(LoginService.class);
			return instance;
		}
	}

	public TextResourcesDTO getText(String teant, String language, String action) throws LoginException;

	public Boolean queryResetPassword(String username);

	public UserDTO loginUser(String username, String password) throws LoginException;
	
	public UserDTO loginFromSessionServer();

	public Boolean logoffUser();
}
