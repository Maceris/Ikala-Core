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
@Getter
public class PluginEvent extends Event {

	/**
	 * The name of the plugin that sent the event, if any.
	 *
	 * @param The plugin sending the event.
	 * @return The plugin sending the event.
	 */
	@SuppressWarnings("javadoc")
	private final String from;
	/**
	 * The name of the plugin that the event is sent to, if any.
	 *
	 * @param The plugin receiving the event.
	 * @return The plugin receiving the event.
	 */
	@SuppressWarnings("javadoc")
	private final String to;

	/**
	 * The content of the event.
	 *
	 * @param The contents of the message.
	 * @return The contents of the message.
	 */
	@SuppressWarnings("javadoc")
	private final String message;

}
