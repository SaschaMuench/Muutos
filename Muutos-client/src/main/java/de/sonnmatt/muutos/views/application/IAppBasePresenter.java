/**
 * 
 */
package de.sonnmatt.muutos.views.application;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

import de.sonnmatt.muutos.interfaces.IPresenter;

/**
 * @author MuenSasc
 *
 */
public interface IAppBasePresenter extends IPresenter {

	void onMunueClick(ClickEvent clickEvent);
	
	void onHide();

	void showViewPort(final Widget viewPort);

}
