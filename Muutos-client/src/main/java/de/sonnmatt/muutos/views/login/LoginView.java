package de.sonnmatt.muutos.views.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.interfaces.IPresenter;
import de.sonnmatt.muutos.views.resources.LoginViewResources;
import de.sonnmatt.processor.GenerateTranslation;

@GenerateTranslation("LoginView.ui.xml")
public class LoginView extends Composite implements ILoginView {

	private Logger logger = Logger.getLogger("Muutos");
	//	Bsp.:	logger.log(Level.SEVERE, "URL key: " + key);
	
	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);
	interface LoginViewUiBinder extends UiBinder<HTMLPanel, LoginView> {
	}

	private ILoginPresenter presenter;

	@UiField(provided = true)
	final LoginViewResources myRen;

	@UiField	Container	loginContainer;
//	@UiField	Modal		loginModal;
	@UiField	FormLabel	loginLabel;
	@UiField	TextBox		loginTextbox;
	@UiField	HelpBlock	loginHelp;
	@UiField	FormLabel	passwordLabel;
	@UiField	Input		passwordInput;
	@UiField	HelpBlock	passwordHelp;
	@UiField	Button		loginBtn;
	@UiField	Anchor		forgetPassword;
	@UiField	Label		footerLine;
	@UiField	FormGroup	groupLogin;
	@UiField	FormGroup	groupPassword;

	public LoginView(TranslationsDTO translation) {
		myRen = new LoginViewResources(translation);
		initWidget(uiBinder.createAndBindUi(this));
		passwordInput.setAutoComplete(true);
	}

	@Override
	public void show() {
//		loginModal.show();
		loginTextbox.setFocus(true);
	}

	@Override
	public void hide() {
//		loginModal.hide();
		//presenter.onHide();
	}

	@UiHandler("forgetPassword")
	void onClickForgetPassword(ClickEvent e) {
		logger.log(Level.FINER, "onClickForgetPassword started");
		presenter.onClickForgetPassword();
	}

	@UiHandler("loginBtn")
	void onClickLoginBtn(ClickEvent event) {
		logger.log(Level.FINER, "onClickLoginBtn started");
		presenter.onClickOk();
		logger.log(Level.FINER, "onClickLoginBtn done");
	}

	@UiHandler(value = { "loginTextbox", "passwordInput" })
	void handleTypedKeys(KeyUpEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			presenter.onClickOk();
	    }
		if (event.getSource().equals(loginTextbox))
			presenter.onTypeLogin(event);
		else if (event.getSource().equals(passwordInput))
			presenter.onTypeLogin(event);
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = (ILoginPresenter) presenter;
	}

	@Override
	public TranslationsDTO getTextValues() {
		return myRen.getTranslationsDTO();
	}

	@Override
	public void setTextValues(TranslationsDTO transDTO) {
		myRen.setTranslationsDTO(transDTO);
	}

	@Override
	public String getLoginTextbox() {
		return loginTextbox.getValue();
	}

	@Override
	public void setLoginTextbox(String loginTextbox) {
		this.loginTextbox.setValue(loginTextbox);
	}

	@Override
	public String getPasswordInput() {
		return this.passwordInput.getValue();
	}

	@Override
	public void setPasswordInput(String passwordInput) {
		this.passwordInput.setValue(passwordInput);
	}

	@Override
	public String getLoginHelp() {
		return loginHelp.getText();
	}

	@Override
	public void setLoginHelp(String loginHelp) {
		this.loginHelp.setText(loginHelp);
	}

	@Override
	public String getPasswordHelp() {
		return passwordHelp.getText();
	}

	@Override
	public void setPasswordHelp(String passwordHelp) {
		this.passwordHelp.setText(passwordHelp);
	}

	@Override
	public ValidationState getLoginValidationState() {
		return groupLogin.getValidationState();
	}

	@Override
	public void setLoginValidationState(ValidationState validationState) {
		this.groupLogin.setValidationState(validationState);
	}

	@Override
	public ValidationState getPasswordValidationState() {
		return groupPassword.getValidationState();
	}

	@Override
	public void setPasswordValidationState(ValidationState validationState) {
		this.groupPassword.setValidationState(validationState);
	}

	@Override
	public void resetValidationsAndHelp() {
		this.groupLogin.setValidationState(ValidationState.NONE);
		this.groupPassword.setValidationState(ValidationState.NONE);
		this.loginHelp.setText("");
		this.passwordHelp.setText("");
	}

	@Override
	public void popupMessage(String value, NotifyType notifyType) {
		NotifySettings settings = NotifySettings.newSettings();
		settings.setPlacement(NotifyPlacement.TOP_CENTER);
		settings.setType(NotifyType.INFO);
		settings.setAllowDismiss(false);
		settings.setZIndex(1060);
		Notify.notify(value, settings);
	}

}
