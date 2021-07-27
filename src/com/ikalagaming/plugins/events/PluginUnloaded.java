package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is unloaded.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
@Getter
public class PluginUnloaded extends Event {

	/**
	 * The plugin that was just unloaded.
	 * 
	 * @param The name of the plugin that was unloaded.
	 * @return The name of the plugin that was unloaded.
	 */
	@SuppressWarnings("javadoc")
	private String thePlugin;

}
