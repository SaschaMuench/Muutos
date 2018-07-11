package de.sonnmatt.muutos.views.login;

import static de.sonnmatt.muutos.enums.TextSections.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import de.sonnmatt.muutos.AppLocalData;
import de.sonnmatt.muutos.URLHandler;
import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.enums.Tokens;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.events.BusyEvent;
import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;
import de.sonnmatt.muutos.rpc.LoginService;

public class LoginPresenter implements ILoginPresenter {

	private ILoginView view;
	private HandlerManager eventBus;
	private TextResourcesDTO texts;

	private Logger logger = Logger.getLogger("Muutos");
	private static final String className = "LoginPresenter.";

	public ILoginView getLoginView() {
		return view;
	}

	public void setLoginView(ILoginView view) {
		this.view = view;
	}

	public LoginPresenter(final ILoginView view, HandlerManager eventBus, TextResourcesDTO result) {
		logger.log(Level.FINER, className + "LoginPresenter() : ENTRY");
		this.eventBus = eventBus;
		this.view = view;
		this.texts = result;
		view.setPresenter(this);
		logger.log(Level.FINER, className + "LoginPresenter() : EXIT");
	}

	/**
	 * @param loginView
	 * @param eventBus
	 */
	public LoginPresenter(LoginView loginView, HandlerManager eventBus) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClickOk() {
		// TODO Auto-generated method stub
		logger.log(Level.FINER, className + "onClickOk() : ENTRY");
		String username = view.getLoginTextbox();
		String password = view.getPasswordInput();
		if (username.isEmpty() || username.equals(null) || username.length() < 3) {
			// user name not in valid range
			view.popupMessage(texts.get(LOGIN + ".view.userNameNeeded"), NotifyType.WARNING);
			view.setLoginHelp(texts.get(LOGIN + ".view.userNameNeeded"));
			view.setLoginValidationState(ValidationState.ERROR);
		}
		if (password.isEmpty() || password.equals(null) || password.length() < 3) {
			// password not in valid range
			view.popupMessage(texts.get(LOGIN + ".view.passwordNeeded"), NotifyType.WARNING);
			view.setPasswordHelp(texts.get(LOGIN + ".view.passwordNeeded"));
			view.setPasswordValidationState(ValidationState.ERROR);
		}
		if (!(username.isEmpty() || username.equals(null) || username.length() < 3)
				&& !(password.isEmpty() || password.equals(null) || password.length() < 3)) {
			// Try to validate user
			eventBus.fireEvent(new BusyEvent(BusyEvents.start));
			logger.log(Level.FINER, className + "onClickOk() : call loginUser");
			LoginService.Util.getInstance().loginUser(username, password, new AsyncCallback<UserDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, className + "onClickLogin() : onFailure", new Throwable(caught));
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
					view.popupMessage(
							texts.get(LOGIN + ".view.communicationError", caught.getMessage()),
							NotifyType.DANGER);
				}

				@Override
				public void onSuccess(UserDTO result) {
					if (result.get(UserFields.SessionID) != null) {
						AppLocalData.storeSessionID(result.get(UserFields.SessionID));
						AppLocalData.setUserDTO(result);
						logger.log(Level.FINER,
								className + "onClickOk() : onSuccess SessionID: " + result.get(UserFields.SessionID));
						logger.log(Level.FINER, className + "onClickOk() : onSuccess ReadSessionID: "
								+ AppLocalData.readCookieSessionID());
						logger.log(Level.FINER,
								className + "onClickOk() : onSuccess GetSessionID: " + AppLocalData.getSessionID());
						URLHandler urlHandler = new URLHandler(Window.Location.getHash());
						urlHandler.setUrlToken(Tokens.APP);
						History.newItem(urlHandler.getUrl());
					} else {
						view.popupMessage(texts.get(LOGIN + ".view.userOrPasswordWrong"),
								NotifyType.WARNING);
						eventBus.fireEvent(new BusyEvent(BusyEvents.end));
						view.setPasswordHelp(texts.get(LOGIN + ".view.userOrPasswordWrong"));
						view.setPasswordValidationState(ValidationState.ERROR);
					}
				}
			});
		}
	}

	@Override
	public void onClickForgetPassword() {
		logger.log(Level.FINER, className + "onClickForgetPassword() : ENTRY");
		String username = view.getLoginTextbox();
		if (username.isEmpty() || username.equals(null) || username.length() < 3) {
			view.popupMessage(texts.get(LOGIN + ".view.userNameNeeded"), NotifyType.WARNING);
			view.setLoginHelp(texts.get(LOGIN + ".view.userNameNeeded"));
			view.setLoginValidationState(ValidationState.ERROR);
		} else {
			eventBus.fireEvent(new BusyEvent(BusyEvents.start));
			LoginService.Util.getInstance().queryResetPassword(username, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, className + "onClickForgetPassword() : onFailure", new Throwable(caught));
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
					view.popupMessage(
							texts.get(LOGIN + ".view.communicationError", caught.getMessage()),
							NotifyType.DANGER);
				}

				@Override
				public void onSuccess(Boolean result) {
					logger.log(Level.FINER, className + "onClickForgetPassword() : onSuccess");
					eventBus.fireEvent(new BusyEvent(BusyEvents.end));
					view.popupMessage(texts.get(LOGIN + ".view.PasswordRequested"),
							NotifyType.INFO);
				}
			});
		}

	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		Window.setTitle(texts.get(LOGIN + ".view.windowTitle"));
		view.show();
	}

	@Override
	public void onHide() {
		// nothing to do
		view.hide();
	}

	@Override
	public void onTypeLogin(KeyUpEvent event) {
		view.resetValidationsAndHelp();
	}

	@Override
	public void onTypePassword(KeyUpEvent event) {
		view.resetValidationsAndHelp();
	}

}
