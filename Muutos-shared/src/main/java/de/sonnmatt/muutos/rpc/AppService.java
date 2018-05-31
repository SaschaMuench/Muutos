/**
 * 
 */
package de.sonnmatt.muutos.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;

/**
 * @author MuenSasc
 * functions to process datatransfer for the application
 */
@RemoteServiceRelativePath("appservice")
public interface AppService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static AppServiceAsync instance;

		public static AppServiceAsync getInstance() {
			if (instance == null) instance = GWT.create(AppService.class);
			return instance;
		}
	}
	
	public DataDTO getData(String sessionID);
	
	public TranslationsDTO getText(String sessionID);
	
	public ViewDTO getView(String sessionID);
	
}
