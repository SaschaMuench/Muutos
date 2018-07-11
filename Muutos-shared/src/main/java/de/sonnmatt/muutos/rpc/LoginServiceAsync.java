/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;

/**
 * @author MuenSasc
 *
 */
public interface LoginServiceAsync {

	void getText(String teant, String lanCode, String action, AsyncCallback<TextResourcesDTO> asyncCallback);

	void queryResetPassword(String username, AsyncCallback<Boolean> asyncCallback);

	void loginUser(String username, String password, AsyncCallback<UserDTO> asyncCallback);
	
	void loginFromSessionServer(AsyncCallback<UserDTO> asyncCallback);
	
	void logoffUser(AsyncCallback<Boolean> asyncCallback);
}
