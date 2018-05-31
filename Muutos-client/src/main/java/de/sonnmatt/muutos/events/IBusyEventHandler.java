package de.sonnmatt.muutos.events;

import com.google.gwt.event.shared.EventHandler;

public interface IBusyEventHandler extends EventHandler {
	void onBusyChanged(BusyEvent busyEvent);
}
