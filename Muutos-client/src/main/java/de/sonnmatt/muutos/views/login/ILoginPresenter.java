/**
 * 
 */
package de.sonnmatt.muutos.views.login;

import com.google.gwt.event.dom.client.KeyUpEvent;

import de.sonnmatt.muutos.interfaces.IPresenter;

/**
 * @author MuenSasc
 *
 */
public interface ILoginPresenter extends IPresenter {

	void onClickOk();

	void onClickForgetPassword();

	void onTypeLogin(KeyUpEvent event);
	
	void onTypePassword(KeyUpEvent event);
	
	void onHide();
}
