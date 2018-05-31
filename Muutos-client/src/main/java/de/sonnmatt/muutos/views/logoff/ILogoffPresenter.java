package de.sonnmatt.muutos.views.logoff;

import com.google.gwt.user.client.ui.HasWidgets;

import de.sonnmatt.muutos.interfaces.IPresenter;

public interface ILogoffPresenter extends IPresenter {

	ILogoffView getLogoffView();

	void setLogoffView(ILogoffView view);

	void onClickLogin();

	void onClickClose();

	void go(HasWidgets container);

	void onHide();

}