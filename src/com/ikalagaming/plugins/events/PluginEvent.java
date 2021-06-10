package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An event that relates to plugins. There is no guarantee that *only* the
 * intended plugin will receive the message.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class PluginEvent extends Event {

	/**
	 * The name of the plugin that sent the event, if any.
	 */
	@Getter
	private final String from;
	/**
	 * The name of the plugin that the event is sent to, if any.
	 */
	@Getter
	private final String to;

	/**
	 * The content of the event.
	 */
	@Getter
	private final String message;

}
