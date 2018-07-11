package de.sonnmatt.muutos.views.login;

import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.interfaces.IView;

public interface ILoginView extends IView {

	public TextResourcesDTO getTextValues();

	public void setTextValues(TextResourcesDTO texts);

	public String getLoginTextbox();

	public void setLoginTextbox(String loginTextbox);

	public String getPasswordInput();

	public void setPasswordInput(String passwordInput);

	public String getLoginHelp();

	public void setLoginHelp(String loginHelp);

	public String getPasswordHelp();

	public void setPasswordHelp(String passwordHelp);

	public ValidationState getLoginValidationState();

	public void setLoginValidationState(ValidationState validationState);

	public ValidationState getPasswordValidationState();

	public void setPasswordValidationState(ValidationState validationState);

	public void resetValidationsAndHelp();
	
	public void popupMessage(String value, NotifyType notifyType);
	
}
