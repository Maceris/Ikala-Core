package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

/**
 * An event that relates to plugins.
 *
 * @author Ches Burks
 *
 */
public class PluginEvent extends Event {

	/**
	 * The name of the plugin that sent the event, if any.
	 */
	private final String pluginTypeFrom;
	/**
	 * The name of the plugin that the event is sent to, if any.
	 */
	private final String pluginTypeTo;

	/**
	 * The content of the event.
	 */
	private final String message;

	/**
	 * Creates a new {@link PluginEvent} with the supplied parameters. There is
	 * no guarantee that only the intended plugin will receive the message.
	 *
	 * @param from the Plugin type of the sender
	 * @param to the Plugin type of the intended receiver
	 * @param theMessage the data to transfer
	 */
	public PluginEvent(String from, String to, String theMessage) {
		this.pluginTypeFrom = from;
		this.pluginTypeTo = to;
		this.message = theMessage;
	}

	/**
	 * Returns the name of the plugin that sent the message, if any.
	 *
	 * @return the name of the plugin
	 */
	public String getFrom() {
		return this.pluginTypeFrom;
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
	 * Returns the name of the plugin that is intended to receive the message,
	 * if any.
	 *
	 * @return the name of the plugin
	 */
	public String getTo() {
		return this.pluginTypeTo;
	}

}
