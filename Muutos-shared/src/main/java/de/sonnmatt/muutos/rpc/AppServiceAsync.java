/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;
import de.sonnmatt.muutos.exceptions.GeneralException;
import de.sonnmatt.muutos.exceptions.LoginException;

/**
 * @author MuenSasc
 *
 */
public interface AppServiceAsync {

	void getData(String sessionID, AsyncCallback<DataDTO> asyncCallback) throws LoginException, GeneralException;
	
	void getText(String sessionID, AsyncCallback<TextResourcesDTO> asyncCallback);
	
	void getView(String sessionID, AsyncCallback<ViewDTO> asyncCallback);

}
