/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.enums.TranslationSections;

/**
 * @author MuenSasc
 *
 */
public interface LoginServiceAsync {

	void getText(String lanCode, TranslationSections action, AsyncCallback<TranslationsDTO> asyncCallback);

	void queryResetPassword(String username, AsyncCallback<Boolean> asyncCallback);

	void loginUser(String username, String password, AsyncCallback<UserDTO> asyncCallback);
	
	void logoffUser(AsyncCallback<Boolean> asyncCallback);
}
