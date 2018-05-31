package de.sonnmatt.muutos.rpc.logging;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;

/**
 * @author MuenSasc
 *
 */
public interface ClientLoggingAsync {

	void log(String logLevel, String message, AsyncCallback<String> asyncCallback);
	
	void log(String logLevel, String sessionID, String message, AsyncCallback<String> asyncCallback);
	
	void getText(String lanCode, AsyncCallback<TranslationsDTO> asyncCallback);

	void queryResetPassword(String username, AsyncCallback<Boolean> asyncCallback);

	void loginUser(String username, String password, AsyncCallback<UserDTO> asyncCallback);
}
