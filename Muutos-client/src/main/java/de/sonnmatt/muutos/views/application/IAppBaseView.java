package de.sonnmatt.muutos.views.application;

import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.interfaces.IView;

public interface IAppBaseView extends IView {

	public TranslationsDTO getTextValues();

	public void setTextValues(TranslationsDTO transDTO);

	public void popupMessage(String value, NotifyType notifyType);

	public void setAppViewPort(Widget viewPort);

	public Widget getAppViewPort();

}
