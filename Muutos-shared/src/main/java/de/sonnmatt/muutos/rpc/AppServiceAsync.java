/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;

/**
 * @author MuenSasc
 *
 */
public interface AppServiceAsync {

	void getData(String sessionID, AsyncCallback<DataDTO> asyncCallback);
	
	void getText(String sessionID, AsyncCallback<TranslationsDTO> asyncCallback);
	
	void getView(String sessionID, AsyncCallback<ViewDTO> asyncCallback);

}
