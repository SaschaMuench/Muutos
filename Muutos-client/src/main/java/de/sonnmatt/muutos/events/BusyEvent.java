/**
 * 
 */
package de.sonnmatt.muutos.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author MuenSasc
 *
 */
public class BusyEvent extends GwtEvent<IBusyEventHandler> {

	public enum BusyEvents {
		start, end
	}
	public static Type<IBusyEventHandler> TYPE = new Type<IBusyEventHandler>();
	
	private final BusyEvents eventType;
	
	public BusyEvent(BusyEvents startOrEnd) {
		super();
		this.eventType = startOrEnd;
	}
	
	@Override
	public Type<IBusyEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IBusyEventHandler handler) {
		handler.onBusyChanged(this);
	}

	public BusyEvents getBusyEventType() {
		return this.eventType;
	}
}
