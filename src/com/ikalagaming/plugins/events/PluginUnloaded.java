package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

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
	 * @param The plugin that was unloaded.
	 * @return The plugin that was unloaded.
	 */
	@SuppressWarnings("javadoc")
	private Plugin thePlugin;

}
