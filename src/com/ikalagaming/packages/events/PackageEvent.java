package com.ikalagaming.packages.events;

import com.ikalagaming.event.Event;

/**
 * An event that relates to packages.
 *
 * @author Ches Burks
 *
 */
public class PackageEvent extends Event {

	/**
	 * The name of the package that sent the event, if any.
	 */
	private final String packageTypeFrom;
	/**
	 * The name of the package that the event is sent to, if any.
	 */
	private final String packageTypeTo;

	/**
	 * The content of the event.
	 */
	private final String message;

	/**
	 * Creates a new {@link PackageEvent} with the supplied parameters. There is
	 * no guarantee that only the intended package will receive the message.
	 *
	 * @param from the Package type of the sender
	 * @param to the Package type of the intended receiver
	 * @param theMessage the data to transfer
	 */
	public PackageEvent(String from, String to, String theMessage) {
		this.packageTypeFrom = from;
		this.packageTypeTo = to;
		this.message = theMessage;
	}

	/**
	 * Returns the name of the package that sent the message, if any.
	 *
	 * @return the name of the package
	 */
	public String getFrom() {
		return this.packageTypeFrom;
	}

	/**
	 * Returns the message transmitted.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Returns the name of the package that is intended to receive the message,
	 * if any.
	 *
	 * @return the name of the package
	 */
	public String getTo() {
		return this.packageTypeTo;
	}

}
