package de.sonnmatt.muutos.views.application;

import static de.sonnmatt.muutos.enums.TextSections.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.sonnmatt.muutos.DTOs.TextResourcesDTO;

public class AppBasePresenter implements IAppBasePresenter {

	private IAppBaseView	view;
	private HandlerManager	eventBus;
	private TextResourcesDTO	transDTO;

	private Logger log = Logger.getLogger("Muutos");
	private static final String className = "AppBasePresenter";
	
	public IAppBaseView getLoginView() {
		return view;
	}

	public void setLoginView(IAppBaseView view) {
		this.view = view;
	}

	public AppBasePresenter(final IAppBaseView view, HandlerManager eventBus, TextResourcesDTO transDTO) {
		log.log(Level.FINER, className + ".AppBasePresenter() : ENTRY");
		this.eventBus = eventBus;
		this.view = view;
		this.transDTO = transDTO;
		view.setPresenter(this);
		log.log(Level.FINER, className + ".AppBasePresenter() : EXIT");
	}

//	/**
//	 * @param loginView
//	 * @param eventBus
//	 */
//	public AppBasePresenter(IAppBaseView view, HandlerManager eventBus) {
//		this.eventBus = eventBus;
//		this.view = view;
//		//this.transDTO = transDTO;
//		view.setPresenter(this);
//	}

	@Override
	public void go(final HasWidgets container) {
		log.log(Level.FINER, className + ".go() : ENTRY");
		container.clear();
		container.add(view.asWidget());
		Window.setTitle(transDTO.get(APP_BASE + ".view.windowTitle"));
		view.show();
		log.log(Level.FINER, className + ".go() : EXIT");
	}
	
	@Override
	public void showViewPort(final Widget viewPort) {
		log.log(Level.FINER, className + ".showViewPort() : ENTRY");
		Window.setTitle(transDTO.get(APP_BASE + ".view.windowTitle"));
		view.setAppViewPort(viewPort);
		log.log(Level.FINER, className + ".showViewPort() : EXIT");
	}
	

	@Override
	public void onHide() {
		// nothing to do
	}

	@Override
	public void onMunueClick(ClickEvent clickEvent) {
		// TODO Auto-generated method stub
		
	}

}
