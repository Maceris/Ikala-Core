package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

/**
 * Fired when (after) a plugin is unloaded.
 *
 * @author Ches Burks
 *
 */
public class PluginUnloaded extends Event {

	/**
	 * The plugin that was just unloaded.
	 */
	private Plugin thePlugin;

	/**
	 * Creates a new {@link PluginUnloaded} for the given plugin.
	 *
	 * @param unloaded the plugin that has been unloaded
	 *
	 */
	public PluginUnloaded(Plugin unloaded) {
		this.thePlugin = unloaded;
	}

	/**
	 * Returns a reference to the plugin was unloaded.
	 *
	 * @return the name of the plugin
	 */
	public Plugin getPlugin() {
		return this.thePlugin;
	}

}
