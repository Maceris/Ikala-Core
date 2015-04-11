package com.ikalagaming.logging.events;

import com.ikalagaming.event.Event;

/**
 * A message or error was logged and needs to be recorded. Dispatched by the
 * logging system after it constructs a message from logs if the logs are of
 * high enough level.
 *
 * @author Ches Burks
 *
 */
public class DisplayLog extends Event {

	private final String theMessage;

	/**
	 * Creates an event to alert any interfaces that might report errors or logs
	 * to the user or developer.
	 *
	 * @param message the info to store or display
	 */
	public DisplayLog(String message) {
		this.theMessage = message;

	}

	/**
	 * Returns a string containing the information to be stored or displayed.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.theMessage;
	}

}
