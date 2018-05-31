/**
 * 
 */
package de.sonnmatt.muutos.events;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.RootPanel;

import de.sonnmatt.muutos.events.BusyEvent.BusyEvents;

/**
 * @author MuenSasc
 *
 */
public class BusyEventHandler implements IBusyEventHandler {

	private Logger log = Logger.getLogger("Muutos");
	private static final String className = "BusyEventHandler.";
	
	private int busyCounter = 0;

	@Override
	public void onBusyChanged(BusyEvent busyEvent) {
		log.log(Level.FINER, className + "onBusyChanged() : " + busyEvent.getSource());
		if (busyEvent.getBusyEventType().equals(BusyEvents.start)) {
			log.log(Level.FINER, className +  "onBusyChanged() : BusyEvents.start: " + busyCounter);
			busyCounter += 1;
			// show "loading..."
			RootPanel.get("spinner").setVisible(true);
		} else {
			log.log(Level.FINER, className +  "onBusyChanged() : BusyEvents.end: " + busyCounter);
			if (busyCounter > 1) {
				busyCounter -= 1;
			} else {
				busyCounter = 0;
				// hide "loading..."
				RootPanel.get("spinner").setVisible(false);
			}
		}
	}

}
