package de.sonnmatt.muutos.views.logoff;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.interfaces.IPresenter;
import de.sonnmatt.muutos.interfaces.IView;

public interface ILogoffView extends IView {

	void show();

	void hide();

	void setPresenter(IPresenter presenter);

	TextResourcesDTO getTextValues();

	void setTextValues(TextResourcesDTO transDTO);

}