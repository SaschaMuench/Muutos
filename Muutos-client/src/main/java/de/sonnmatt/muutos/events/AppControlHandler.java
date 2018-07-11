/**
 * 
 */
package de.sonnmatt.muutos.events;

import static de.sonnmatt.muutos.enums.TextSections.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import de.sonnmatt.muutos.AppLocalData;
import de.sonnmatt.muutos.URLHandler;
import de.sonnmatt.muutos.DTOs.DataDTO;
import de.sonnmatt.muutos.DTOs.DataDTO.dataTypes;
import de.sonnmatt.muutos.enums.Params;
import de.sonnmatt.muutos.enums.Tokens;
import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;
import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;
import de.sonnmatt.muutos.exceptions.GeneralException;
import de.sonnmatt.muutos.exceptions.LoginException;
import de.sonnmatt.muutos.rpc.AppService;
import de.sonnmatt.muutos.rpc.LoginService;
import de.sonnmatt.muutos.views.application.AppBasePresenter;
import de.sonnmatt.muutos.views.application.AppBaseView;
import de.sonnmatt.muutos.views.application.IAppBasePresenter;
import de.sonnmatt.muutos.views.application.StatisticApp;
import de.sonnmatt.muutos.views.login.LoginPresenter;
import de.sonnmatt.muutos.views.login.LoginView;
import de.sonnmatt.muutos.views.logoff.LogoffPresenter;
import de.sonnmatt.muutos.views.logoff.LogoffView;

/**
 * @author MuenSasc Class for managing the pages of the application
 */
public class AppControlHandler implements IAppControlHandler, ValueChangeHandler<String> {

	private Logger log = Logger.getLogger("Muutos");
	private static final String className = "AppControlHandler";
	
	private HandlerManager eventBus;
	private LoginPresenter loginPresenter;
	private LogoffPresenter logoffPresenter;
	private IAppBasePresenter appBasePresenter;
	private HasWidgets container;
	private String language;
	private String tenant;

	public AppControlHandler(HasWidgets container, HandlerManager manager, String language) {
		log.log(Level.FINER, className + " AppControlHandler()");
		this.eventBus = manager;
		this.language = language;
		this.container = container;
	}

	/**
	 * @param page
	 *            widget where the new page is added
	 */
	public void go(HasWidgets page) {
		this.container = page;
		loginPresenter.go(page);
	//	eventBus.fireEvent(new BusyEvent(BusyEvents.end));
	}

	@Override
	public void bindEvents() {
		History.addValueChangeHandler(this);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		log.log(Level.FINER, className + ".onValueChange() : " + event.getValue());
		
		URLHandler urlHandler = new URLHandler(Window.Location.getHash());
		tenant = urlHandler.getParameter(Params.COMPANY);
		Tokens urlTocken = urlHandler.getUrlToken();
		log.log(Level.FINER, className + ".onValueChange() urlTocken: " + urlTocken.toString());
		switch (urlHandler.getUrlToken()) {
		case LOGIN:
			doLogin();	
			break;
		case APP:
			if (AppLocalData.getSessionID().isEmpty()) {
				log.log(Level.FINER, className + ".onValueChange(): no session ID --> Login");
				urlHandler.setUrlToken(Tokens.LOGIN);
				History.newItem(urlHandler.getUrl());
				break;
			}
			if (urlHandler.getUrlParammeterMap().size() == 1) {
				log.log(Level.FINER, className + ".onValueChange(): no params --> Appstart");
				doAppStart();				
			} else {
				log.log(Level.FINER, className + ".onValueChange(): parameter form: " + urlHandler.getParameter(Params.FORM).toLowerCase());
				switch (urlHandler.getParameter(Params.FORM).toLowerCase()) {
				case "statistic":
					log.log(Level.FINER, className + ".onValueChange(): form = statistic");
					StatisticApp staticticApp = new StatisticApp("Test");
					appBasePresenter.showViewPort(staticticApp.asWidget());
					break;
				case "usermanagement":
					break;
					
				default:
					// TODO handle not implemented UrlParams
					log.log(Level.SEVERE, className + " .onValueChange()", new Throwable("unknown UrlParam Base: " + urlHandler.getParameter(Params.FORM)));
					break;
				
				}
			}
			break;
		case LOGOFF:
			doLogoff(false);
			break;
		case TIMEOUT:
			doLogoff(true);
			break;
			
		default:
			// TODO handle not implemented UrlTocken
			log.log(Level.SEVERE, className + ".onValueChange()", new Throwable("unknown UrlToken: " + urlHandler.getUrlToken()));
			break;
		}
	}

	private void doAppStart() {
		log.log(Level.FINER, className + ".doAppStart() : ENTRY");
		try {
			AppService.Util.getInstance().getData(AppLocalData.getSessionID(), new AsyncCallback<DataDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log.log(Level.FINER, className + ".doAppStart()", new Throwable(caught.toString()));
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
				}

				@Override
				public void onSuccess(DataDTO result) {
					log.log(Level.FINER, className + ".doAppStart() : onSuccess()");
					
					loginPresenter.onHide();
					
					TextResourcesDTO transDTO = (TextResourcesDTO) result.get(dataTypes.Translation);
					ViewDTO viewDTO = (ViewDTO) result.get(dataTypes.View);
					appBasePresenter = new AppBasePresenter(new AppBaseView(transDTO, viewDTO, AppLocalData.getUserDTO()), eventBus, transDTO);
					appBasePresenter.go(container);
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
				}
				
			});
		} catch (LoginException | GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doLogin() {
		log.log(Level.FINER, className + ".doLogin() : ENTRY");
		
		try {
			LoginService.Util.getInstance().getText(tenant, language, LOGIN, new AsyncCallback<TextResourcesDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log.log(Level.FINER, className + ".doLogin()", new Throwable(caught.toString()));
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
				}

				@Override
				public void onSuccess(TextResourcesDTO result) {
					log.log(Level.FINER, className + ".doLogin() : onSuccess()");

					loginPresenter = new LoginPresenter(new LoginView(result), eventBus, result);
					loginPresenter.go(container);
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, className + ".doLogin()", new Throwable(e.getMessage()));
			e.printStackTrace();
		}

	}

	private void doLogoff(boolean timeout) {
		log.log(Level.FINER, className + ".doLogoff() : " + String.valueOf(timeout));
		String action = timeout == true ? TIMEOUT : LOGOFF;
		try {
			LoginService.Util.getInstance().getText(tenant, language, action, new AsyncCallback<TextResourcesDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					log.log(Level.FINER, className + ".doLogoff()", new Throwable(caught.toString()));
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
				}

				@Override
				public void onSuccess(TextResourcesDTO result) {
					log.log(Level.FINER, className + ".doLogoff() : onSuccess()");
					logoffPresenter = new LogoffPresenter(new LogoffView(result), eventBus, result);
					logoffPresenter.go(container);
					LoginService.Util.getInstance().logoffUser(new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable arg0) {
							eventBus.fireEvent(new BusyEvent(BusyEvents.end));
						}

						@Override
						public void onSuccess(Boolean arg0) {
							eventBus.fireEvent(new BusyEvent(BusyEvents.end));
						}});
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, className + ".doLogin()", new Throwable(e.getMessage()));
			e.printStackTrace();
		}
	}

}
