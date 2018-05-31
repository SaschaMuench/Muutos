package de.sonnmatt.muutos.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IView extends IsWidget {

	void setPresenter(IPresenter presenter);

	void show();

	void hide();
}
