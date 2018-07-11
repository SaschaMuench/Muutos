/**
 * 
 */
package de.sonnmatt.muutos;

import static de.sonnmatt.muutos.enums.TextSections.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.events.BusyEvent;
import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;
import de.sonnmatt.muutos.rpc.LoginService;
import de.sonnmatt.muutos.views.login.LoginPresenter;
import de.sonnmatt.muutos.views.login.LoginView;

/**
 * @author MuenSasc Class for managing the pages of the application
 */
public class AppController {
	/**
	 * Create a remote service proxy to talk to the server-side Login service.
	 */
	//private final LoginServiceAsync LoginService = GWT.create(LoginService.class);

	private Logger log = Logger.getLogger("Muutos");
	
	private HandlerManager eventBus;
	private LoginPresenter loginPage;
	private HasWidgets container;
	private String language;

	public AppController(HandlerManager manager, String tenant, String language) {
		this.eventBus = manager;
		this.language = language;

		log.log(Level.FINER, "AppController started");
		LoginService.Util.getInstance().getText(tenant, language, APP_BASE, new AsyncCallback<TextResourcesDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log.log(Level.SEVERE, "AppController getText failed: " + caught.getMessage());
			}

			@Override
			public void onSuccess(TextResourcesDTO result) {
				log.log(Level.FINER, "AppController getText succeded: " + result.size());
				loginPage = new LoginPresenter(new LoginView(result), eventBus);
				bindEvents();
			}
		});

	}

	/**
	 * @param page
	 *            widget where the new page is added
	 */
	public void go(HasWidgets page) {
		this.container = page;
		loginPage.go(page);
		eventBus.fireEvent(new BusyEvent(BusyEvents.end));
	}

	private void bindEvents() {
		// TODO Auto-generated method stub

	}

}