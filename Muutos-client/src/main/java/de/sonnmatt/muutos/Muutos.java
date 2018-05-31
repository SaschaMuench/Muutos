package de.sonnmatt.muutos;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import de.sonnmatt.muutos.enums.Params;
import de.sonnmatt.muutos.enums.Tokens;
import de.sonnmatt.muutos.events.AppControlHandler;
import de.sonnmatt.muutos.events.BusyEvent;
import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;
import de.sonnmatt.muutos.events.BusyEventHandler;
import de.sonnmatt.muutos.events.IAppControlHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Muutos implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private Logger logger = Logger.getLogger("Muutos");

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	// private final GreetingServiceAsync greetingService =
	// GWT.create(GreetingService.class);
	// private final LoginServiceAsync loginService =
	// GWT.create(LoginService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get("spinner").setVisible(true);
		HandlerManager eventBus = new HandlerManager(null);
		eventBus.addHandler(BusyEvent.TYPE, new BusyEventHandler());

		//eventBus.fireEvent(new BusyEvent(BusyEvents.start));
		IAppControlHandler app = new AppControlHandler(RootPanel.get("content"), eventBus, getLanguage());
		app.bindEvents();

		String token = History.getToken();
		if (token.isEmpty() || token.equals(null)) {
			HashMap<String, String> urlParams = new HashMap<>();
			urlParams.put(Params.COMPANY.toString(), Window.Location.getHostName().split("\\.")[0]);
			URLHandler urlHandler = new URLHandler();
			urlHandler.setUrlToken(Tokens.LOGIN);
			urlHandler.setUrlParameterMap(urlParams);
			//History.newItem(Tokens.LOGIN + "?" + Params.COMPANY + "=" + company + "&");
			History.newItem(urlHandler.getUrl());
		} else {
			History.fireCurrentHistoryState();
		}
	}

	/**
	 * Get preferred language according to browser settings.
	 * https://groups.google.com/forum/#!topic/google-web-toolkit/9eVq1HBZmww
	 * 
	 * @return The preferred language.
	 */
	public static native String getLanguage()
	/*-{
		if (navigator.language == null) {
			if (navigator.userLanguage == null) {
				return "";
			} else {
				return navigator.userLanguage;
			}
		} else {
			return navigator.language;
		}
	}-*/;
}
