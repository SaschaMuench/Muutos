package de.sonnmatt.muutos.events;

import com.google.gwt.event.shared.EventHandler;

public interface IAppEventHandler extends EventHandler {
	void onBusyChanged(BusyEvent busyEvent);
}
