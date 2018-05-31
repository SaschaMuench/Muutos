/**
 * 
 */
package de.sonnmatt.muutos.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author MuenSasc
 *
 */
public class AppEvent extends GwtEvent<IAppEventHandler> {

	public enum AppEvents {
		loggedin, logout
	}
	
	public static Type<IAppEventHandler> TYPE = new Type<IAppEventHandler>();
	private final AppEvents eventType;
	
	public AppEvent(AppEvents appAction) {
		super();
		this.eventType = appAction;
	}

	@Override
	public Type<IAppEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IAppEventHandler handler) {
		// TODO Auto-generated method stub
		
	}

	public AppEvents getAppEventType() {
		return this.eventType;
	}
}
