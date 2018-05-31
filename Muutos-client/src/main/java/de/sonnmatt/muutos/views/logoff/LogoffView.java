package de.sonnmatt.muutos.views.logoff;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import de.sonnmatt.muutos.DTOs.TranslationsDTO;
import de.sonnmatt.muutos.interfaces.IPresenter;
import de.sonnmatt.muutos.views.resources.LogoffViewResources;
import de.sonnmatt.processor.GenerateTranslation;

@GenerateTranslation("LogoffView.ui.xml")
public class LogoffView extends Composite implements ILogoffView  {

	private Logger log = Logger.getLogger("Muutos");
	
	private static LogoffViewUiBinder uiBinder = GWT.create(LogoffViewUiBinder.class);
	interface LogoffViewUiBinder extends UiBinder<HTMLPanel, LogoffView> {
	}

	private ILogoffPresenter presenter;

	@UiField(provided = true)
	final LogoffViewResources myRen;

	@UiField	Button		loginBtn;
	@UiField	Button		closeBtn;
	@UiField	Label		footerLine;

	public LogoffView(TranslationsDTO translation) {
		myRen = new LogoffViewResources(translation);
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void show() {
		loginBtn.setFocus(true);
	}

	public void hide() {
	}

	@UiHandler("loginBtn")
	void onClickLoginBtn(ClickEvent event) {
		log.log(Level.FINER, this.getClass().getSimpleName() + " onClickLoginBtn() : ENTRY");
		presenter.onClickLogin();
		log.log(Level.FINER, "onClickLoginBtn() : DONE");
	}

	@UiHandler("closeBtn")
	void onClickCloseBtn(ClickEvent event) {
		log.log(Level.FINER, this.getClass().getSimpleName() + " onClickCloseBtn() : ENTRY");
		presenter.onClickClose();
		log.log(Level.FINER, "onClickCloseBtn() : DONE");
	}

	public void setPresenter(IPresenter presenter) {
		this.presenter = (ILogoffPresenter) presenter;
	}

	public TranslationsDTO getTextValues() {
		return myRen.getTranslationsDTO();
	}

	public void setTextValues(TranslationsDTO transDTO) {
		myRen.setTranslationsDTO(transDTO);
	}
}
