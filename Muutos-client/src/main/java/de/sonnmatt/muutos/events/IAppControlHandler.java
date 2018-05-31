/**
 * 
 */
package de.sonnmatt.muutos.events;

import com.google.gwt.event.logical.shared.ValueChangeEvent;

/**
 * @author MuenSasc
 *
 */
public interface IAppControlHandler {

	void bindEvents();

	/* (non-Javadoc)
	 * @see de.sonnmatt.muutos.events.IAppControlHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	void onValueChange(ValueChangeEvent<String> event);

}