package de.sonnmatt.muutos.views.logoff;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import de.sonnmatt.muutos.URLHandler;
import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.enums.Tokens;
import de.sonnmatt.muutos.enums.TranslationSections;
import de.sonnmatt.muutos.events.BusyEvent;
import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;

public class LogoffPresenter implements ILogoffPresenter {

	private ILogoffView		view;
	private HandlerManager	eventBus;
	private TranslationsDTO	transDTO;

	private Logger log = Logger.getLogger("Muutos");

	public ILogoffView getLogoffView() {
		return view;
	}

	public void setLogoffView(ILogoffView view) {
		this.view = view;
	}

	public LogoffPresenter(final ILogoffView view, HandlerManager eventBus, TranslationsDTO transDTO) {
		this.eventBus = eventBus;
		this.view = view;
		this.transDTO = transDTO;
		view.setPresenter(this);
	}

	/**
	 * @param loginView
	 * @param eventBus
	 */
	public LogoffPresenter(de.sonnmatt.muutos.views.logoff.ILogoffView logoffView, HandlerManager eventBus) {
		// TODO Auto-generated constructor stub
	}

	public void onClickLogin() {
		log.log(Level.FINER, this.getClass().getSimpleName() + " onClickLogin(): ENTRY");

		eventBus.fireEvent(new BusyEvent(BusyEvents.start));
		URLHandler urlHandler = new URLHandler(Window.Location.getHash());
		urlHandler.setUrlToken(Tokens.LOGIN);
		log.log(Level.FINER, this.getClass().getSimpleName() + " fire event load loginpage");
		History.newItem(urlHandler.getUrl());
	}

	public native void onClickClose() /*-{
		open(location, '_self').close();
	}-*/;

	public void go(final HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		Window.setTitle(transDTO.get(TranslationSections.Logoff + ".view.windowTitle"));
		view.show();
	}

	public void onHide() {
		// nothing to do
		view.hide();
	}
}
