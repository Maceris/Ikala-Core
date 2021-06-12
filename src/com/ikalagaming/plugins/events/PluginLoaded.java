package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is loaded.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
@Getter
public class PluginLoaded extends Event {

	/**
	 * The plugin that was just loaded.
	 * 
	 * @param The name of the plugin that was loaded.
	 * @return The name of the plugin that was loaded.
	 */
	@SuppressWarnings("javadoc")
	private String plugin;

}
